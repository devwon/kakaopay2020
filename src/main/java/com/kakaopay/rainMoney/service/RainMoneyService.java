package com.kakaopay.rainMoney.service;

import com.google.gson.Gson;
import com.kakaopay.rainMoney.constant.ErrCode;
import com.kakaopay.rainMoney.entity.RainMoney;
import com.kakaopay.rainMoney.entity.RainMoneyDetail;
import com.kakaopay.rainMoney.entity.RoomUser;
import com.kakaopay.rainMoney.entity.User;
import com.kakaopay.rainMoney.exception.APIException;
import com.kakaopay.rainMoney.repository.RainMoneyDetailRepository;
import com.kakaopay.rainMoney.repository.RainMoneyRepository;
import com.kakaopay.rainMoney.repository.RoomUserRepository;
import com.kakaopay.rainMoney.repository.UserRepository;
import com.kakaopay.rainMoney.utils.CommonUtils;
import lombok.Builder;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RainMoneyService {
    @Autowired
    RoomUserRepository roomUserRepo;
    @Autowired
    RainMoneyRepository rainMoneyRepo;
    @Autowired
    RainMoneyDetailRepository rainMoneyDetailRepo;
    @Autowired
    UserRepository userRepo;
    public static final long receiveValidMins;
    public static final long getDetailValidDays;
    public static final int  maxRetryCount;

    // 유효 시간, 최대 시도 횟수 관련 정적 초기화문 선언
    static {
        receiveValidMins = 10;  // 10 mins
        getDetailValidDays = 7;      // 7 days
        maxRetryCount = 3;
    }
    CommonUtils utils = new CommonUtils();

    /**
     * 뿌리기 분배
     * @param total_amount 뿌릴 금액
     * @param count 뿌릴 인원
     * @param minAmount 뿌리기건 당 최소 분배 금액
     * @throws RuntimeException 특정 조건에 맞지 않아 분배할 수 없는 경우
     */
    List<Integer> divideRainMoney(Integer total_amount, Integer count, Integer minAmount) throws RuntimeException{
        if (total_amount <= 0) {
            throw new APIException("뿌릴 금액은 0원 보다 커야합니다.", ErrCode.E0106.getCode());
        }
        if (count <= 0) {
            throw new APIException("뿌릴 인원은 0명 보다 커야합니다.", ErrCode.E0106.getCode());
        }
        if (total_amount < count) {
            throw new APIException(ErrCode.E0105.getErrMsg(), ErrCode.E0105.getCode());
        }
        if (minAmount <= 0) {
            throw new APIException("최소 분배 금액은 0원 보다 커야합니다.", ErrCode.E0106.getCode());
        }
        // 최대 분배 금액 = 뿌릴 금액 - 뿌릴 인원 * 최소 분배 금액
        int maxAmount = total_amount - count * minAmount;
        if (maxAmount < 0) {
            throw new APIException("최대 분배 금액은 0원 이상이어야합니다.", ErrCode.E0106.getCode());
        }

        // count-1 만큼 랜덤 숫자를 가지는 정수형 리스트 생성
        // 뿌리기 인원이 N명 이라면, N-1 크기의 리스트
        List<Integer> randomlist = new ArrayList<>();
        for(int i = 0 ; i < count - 1 ; i++) {
            randomlist.add(RandomUtils.nextInt(0, maxAmount));
        }
        // 리스트의 마지막 칸에 최대분배금액을 추가
        randomlist.add(maxAmount);
        // 리스트 오름차순 정렬
        Collections.sort(randomlist);

        // 리스트의 n-1 ~ 1번째 인덱스까지 루프를 돌면서, A[n] = A[n] - A[n-1] 내림차순으로 반복
        // maxAmount가 리스트의 마지막 항목인 randomlist.get(n)이므로
        // 리스트 항목은 [m1, m2-m1, ..., m(n-1) - m(n-2), maxAmount - m(n-1)]
        // 현재까지 리스트 합계는 maxAmount
        for(int i = count - 1; i > 0; i--){
            randomlist.set(i, randomlist.get(i) - randomlist.get(i - 1));
        }
        // 처음에 최소분배금액을 인원수만큼 빼주었기 때문에 리스트의 각 항목의 값을 최소분배금액만큼 더하기
        // 최종 리스트 합계는 maxAmount + (minAmount * count) = total_amount
        for(int n = 0; n < randomlist.size(); n++){
            randomlist.set(n, randomlist.get(n) + minAmount);
        }
        return randomlist;
    }
    /**
     * 뿌리기 생성 및 받은 고객 정보값이 비어있는 뿌리기 분배건 생성
     * @param total_amount 뿌릴 금액
     * @param count 뿌릴 인원
     * @param roomId 뿌린 채팅방 ID
     * @param userId 뿌린 사용자 ID
     * @throws APIException 특정 조건에 맞지 않아 분배할 수 없는 경우
     */
    @Transactional
    public String insertRainMoney(Integer total_amount, Integer count, String roomId, Long userId) throws APIException {

        // 뿌릴 인원이 채팅방 인원수보다 클 경우 에러
        List<RoomUser> roomUserEnt = roomUserRepo.findByIdRoomId(roomId);
        if (roomUserEnt.size() - 1 < count){
            throw new APIException(ErrCode.E0108.getErrMsg(), ErrCode.E0108.getCode());
        }
        int retryCount = 0; // 토큰 중복 에러 발생시 최대 재시도 횟수
        Boolean is_success = false;
        String token = "";
        while (!is_success && retryCount < maxRetryCount) {
            try {
                // 뿌리기 토큰 3자리 랜덤 문자열 생성
                token = utils.createToken(3);
                // 뿌리기 객체 저장을 위한 값 할당
                RainMoney rainMoney = new RainMoney();
                RoomUser roomUser = roomUserRepo.findByIdRoomIdAndIdUserId(roomId, userId);
                rainMoney.setToken(token);
                rainMoney.setRoomUserId(roomUser);
                rainMoney.setTotalAmount(total_amount);
                rainMoney.setCreatedTime(LocalDateTime.now());
                // 뿌리기 객체 저장
                rainMoneyRepo.save(rainMoney);
                is_success = true;
            } catch (Exception e) {
                // TODO: 에러 로깅
                System.out.println(e);
            }
            retryCount++;
        }

        // 최대 시도 횟수 초과하여 토큰 발급 실패 에러
        if (!is_success) {
            throw new APIException(ErrCode.E0001.getErrMsg(), ErrCode.E0001.getCode());
        }

        // 뿌리기 분배건 생성
        int minAmount = 1;
        List<Integer> rainMoneyList = divideRainMoney(total_amount, count, minAmount);
        List<RainMoneyDetail> rainMoneyDetails = new ArrayList<>();
        RainMoney rainMoney = rainMoneyRepo.getOne(token);
        for(int seq = 0; seq < rainMoneyList.size(); seq++ ){
            // 뿌리기 분배건 저장을 위한 값 할당
            RainMoneyDetail rainMoneyDetail = new RainMoneyDetail();
            rainMoneyDetail.setToken(rainMoney);
            rainMoneyDetail.setAmount(rainMoneyList.get(seq));
            rainMoneyDetail.setSeq(seq);
            rainMoneyDetail.setCreatedTime(LocalDateTime.now());
            rainMoneyDetails.add(rainMoneyDetail);
        }

        rainMoneyDetailRepo.saveAll(rainMoneyDetails);

        return token;
    }

    /**
     * 뿌린 금액 받기
     * @param token 뿌리기 토큰
     * @param userId 받을 사용자 ID
     * @return 할당된 분배건의 금액
     * @throws APIException 특정 조건에 맞지 않아 분배할 수 없는 경우
     */
    public int updateRainMoneyDetail(String token, Long userId) throws RuntimeException {
        // 토큰에 해당하는 뿌리기 조회
        Optional<RainMoney> rainMoneyOpt = rainMoneyRepo.findById(token);
        if (rainMoneyOpt.isEmpty()) {
            throw new APIException("TOKEN "+ ErrCode.E0103.getErrMsg(), ErrCode.E0103.getCode());
        }
        // 받기 요청한 사용자 ID에 해당하는 사용자 조회
        Optional<User> requesterOpt = userRepo.findById(userId);
        if (requesterOpt.isEmpty()) {
            throw new APIException("사용자 " + ErrCode.E0103.getErrMsg(), ErrCode.E0103.getCode());
        }
        // 뿌리기 객체
        RainMoney rainMoneyEnt = rainMoneyOpt.get();
        // 사용자 객체
        User requesterEnt = requesterOpt.get();

        // 본인이 뿌린 주체인 경우에는 에러
        if (rainMoneyEnt.getRoomUserId().getId().getUserId().equals(userId)) {
            throw new APIException(ErrCode.E0104.getErrMsg(), ErrCode.E0104.getCode());
        }

        // 받기 유효시간(10분) 초과시 에러
        if (rainMoneyEnt.getCreatedTime().isBefore(LocalDateTime.now().minusMinutes(receiveValidMins))){
            throw new APIException(ErrCode.E0107.getErrMsg(), ErrCode.E0107.getCode());
        }

        // 대화방에 속하지 않은 사용자인 경우
        String roomId = rainMoneyEnt.getRoomUserId().getId().getRoomId();
        List<RoomUser> roomUserList = roomUserRepo.findByIdRoomId(roomId);
        for (int i = roomUserList.size() - 1; i >= 0; i--) {
            if (roomUserList.get(i).getId().getUserId().equals(userId)) {
                break;
            }else{
                if (i == 0){
                    // for문 다 돌았는데 아직도 찾지 못하면 에러
                    throw new APIException(ErrCode.E0302.getErrMsg(), ErrCode.E0302.getCode());
                }
            }
        }

        // 하나의 토큰에 대해 1번 초과하여 받으려고 할 경우
        List<RainMoneyDetail> historyWithTokenAndTargetUser = rainMoneyDetailRepo.findHistoryWithTokenAndTargetUser(rainMoneyEnt, requesterEnt);

        if (historyWithTokenAndTargetUser.size() > 0){
            throw new APIException(ErrCode.E0101.getErrMsg(), ErrCode.E0101.getCode());
        }

        // 분배 가능 항목 조회 (자신이 받은 건도 같이 포함)
        List<RainMoneyDetail> availableDetailList = rainMoneyDetailRepo.findAvailableDetailsWithTokenAndTargetUser(rainMoneyEnt, requesterEnt);
        // 분배 가능 항목 중 하나를 선택해서 받은 상태로 변경.
        // 다른 사용자에게 이미 분배된 경우 계속 재시도
        int count = availableDetailList.size();
        while (count > 0) {
            // 분배할 항목의 인덱스를 다시 랜덤 생성
            // 순서대로 할당하지 않고 랜덤하게 할당하여 타서버와의 할당 충돌 방지
            int idx = RandomUtils.nextInt(0, count);
            RainMoneyDetail rainMoneyDetail = availableDetailList.get(idx);

            boolean is_success = rainMoneyDetailRepo.updateTargetUserId(rainMoneyDetail.getId(), requesterEnt);
            if (is_success) {
                return rainMoneyDetail.getAmount();
            }

            // 이미 다른 사람이 받은 경우, 해당 항목을 리스트에서 삭제
            availableDetailList.remove(idx);
            count = availableDetailList.size();
        }

        // 더 이상 받을 항목이 없는 경우
        throw new APIException(ErrCode.E0107.getErrMsg(), ErrCode.E0107.getCode());
    }
    /**
     * 뿌리기 정보 조회
     * @param token 뿌리기 토큰
     * @param userId 뿌린 사용자 ID
     * @throws APIException 특정 조건에 맞지 않아 분배할 수 없는 경우
     */
    public String getRainMoneyDetail(String token, Long userId) throws APIException {
        // 토큰에 해당하는 뿌리기 조회
        Optional<RainMoney> rainMoneyOpt = rainMoneyRepo.findById(token);
        if (rainMoneyOpt.isEmpty()) {
            throw new APIException("TOKEN "+ ErrCode.E0103.getErrMsg(), ErrCode.E0103.getCode());
        }
        // 뿌리기 객체
        RainMoney rainMoneyEnt = rainMoneyOpt.get();

        // 뿌린 사용자가 아닌 경우
        if (!rainMoneyEnt.getRoomUserId().getId().getUserId().equals(userId)) {
            throw new APIException(ErrCode.E0301.getErrMsg(), ErrCode.E0301.getCode());
        }

        // 조회 유효 기간 초과
        if(rainMoneyEnt.getCreatedTime().isBefore(LocalDateTime.now().minusDays(getDetailValidDays))){
            throw new APIException(ErrCode.E0102.getErrMsg(), ErrCode.E0102.getCode());
        }

        // 받기 완료된 항목들 조회
        List<RainMoneyDetail> finishedDetailList = rainMoneyDetailRepo.findRainMoneyDetail(rainMoneyEnt);
        int receivedAmount = 0;
        List<ReceivedItem> receivedItems = new ArrayList<>();
        for (int n = 0; n < finishedDetailList.size(); n++){
            receivedAmount += finishedDetailList.get(n).getAmount();
            ReceivedItem receivedItem = new ReceivedItem(
                    finishedDetailList.get(n).getAmount(),
                    finishedDetailList.get(n).getTargetUserId().getId());
            receivedItems.add(receivedItem);
        };

        GetRainMoneyDetailDTO result = new GetRainMoneyDetailDTO(
                rainMoneyEnt.getCreatedTime(),
                rainMoneyEnt.getTotalAmount(),
                receivedAmount,
                receivedItems);

        // 결과값 JSON 형식으로 노출해줌
        Gson gson = new Gson();
        String resJson = gson.toJson(result);

        return resJson;
    }
}

/** 뿌리기 상태 조회 결과 항목들 */
@Builder
class GetRainMoneyDetailDTO {
    LocalDateTime createdTime;
    int totalAmount;
    int receivedAmount;
    List<ReceivedItem> receivedList;

};

@Builder
/** 뿌리기 받기 완료된 받은 사용자별 정보 */
class ReceivedItem{
    int amount;
    Long userId;
}