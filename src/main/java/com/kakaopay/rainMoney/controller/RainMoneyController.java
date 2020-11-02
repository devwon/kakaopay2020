package com.kakaopay.rainMoney.controller;

import com.kakaopay.rainMoney.constant.ErrCode;
import com.kakaopay.rainMoney.exception.APIException;
import com.kakaopay.rainMoney.service.RainMoneyService;
import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kakaopay", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class RainMoneyController {
    //사용자 ID, 채팅방 ID 선언
    public static final String USER_ID="X-USER-ID";
    public static final String ROOM_ID="X-ROOM-ID";

    @Autowired
    private RainMoneyService rmService;

    /**
     * 뿌리기 API
     * @param roomId 채팅방 ID
     * @param userId 사용자 ID
     * @return ResponseEntity
     */
    @ResponseBody
    @PostMapping(path = "/rainMoney")
    public ResponseEntity rainMoney(@RequestHeader(USER_ID) Long userId,
                                    @RequestHeader(ROOM_ID) String roomId,
                                    @RequestBody @NotNull CreateBody createBody){
        try {
            if (createBody.total_amount <= 0) {
                throw new APIException("뿌릴 금액은 0원 보다 커야 합니다.", ErrCode.E0106.getCode());
            }
            if (createBody.count <= 0) {
                throw new APIException("뿌릴 인원은 0명 보다 커야 합니다.", ErrCode.E0106.getCode());
            }
            if (createBody.total_amount < createBody.count) {
                throw new APIException(ErrCode.E0105.getErrMsg(), ErrCode.E0105.getCode());
            }
            String token = rmService.insertRainMoney(createBody.total_amount, createBody.count, roomId, userId);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (InternalError | Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 뿌린 금액 받기 API
     * @param roomId 채팅방 ID
     * @param userId 사용자 ID
     * @return ResponseEntity
     */
    @PostMapping(path = "/receive")
    public ResponseEntity receiveRainMoney(@RequestHeader(USER_ID) Long userId,
                                           @RequestHeader(ROOM_ID) String roomId,
                                           @RequestBody @NotNull ReceiveBody receiveBody){
        try {
            int receivedAmount = rmService.updateRainMoneyDetail(receiveBody.token, userId);
            return new ResponseEntity<>(receivedAmount, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (InternalError | Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 뿌리기 정보 조회 API
     * @param roomId 채팅방 ID
     * @param userId 사용자 ID
     * @return ResponseEntity
     */
    @GetMapping(path = "/rainMoney")
    public ResponseEntity rainMoneyDetail(@RequestHeader(USER_ID) Long userId,
                                          @RequestHeader(ROOM_ID) String roomId,
                                          @RequestBody @NotNull ReceiveBody receiveBody){
        try {
            String detail = rmService.getRainMoneyDetail(receiveBody.token, userId);
            return new ResponseEntity<>(detail, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        } catch (InternalError | Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


/** 뿌리기 요청에 사용되는 Body */
@Data
class CreateBody{
    int total_amount;
    int count;
};
@Data
class ReceiveBody{
    String token;
};