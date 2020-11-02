package com.kakaopay.rainMoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kakaopay.rainMoney.controller.RainMoneyController;
import com.kakaopay.rainMoney.repository.RainMoneyDetailRepository;
import com.kakaopay.rainMoney.repository.RainMoneyRepository;
import com.kakaopay.rainMoney.repository.RoomUserRepository;
import com.kakaopay.rainMoney.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RainMoneyApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	RoomUserRepository roomUserRepo;
	@Autowired
	RainMoneyRepository rainMoneyRepo;
	@Autowired
	RainMoneyDetailRepository rainMoneyDetailRepo;
	@Autowired
	UserRepository userRepo;

	public static final String ROOMID = "X-ROOM-ID";
	public static final String USERID = "X-USER-ID";

	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(RainMoneyController.class).build();
	}

	/**
	 * 뿌리기 API 테스트(정상)
	 */
	@Test
	void rainMoneyTest() throws Exception {

		// given
		RequestDTO requestrainMoney = new RequestDTO();
		requestrainMoney.count = 3;
		requestrainMoney.total_amount = 300;

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(requestrainMoney);

		// When
		mockMvc.perform(
				post("/kakaopay/rainMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());

		// Then
		// TODO:
		// return 201 Created
	}

	/**
	 * 뿌리기 API 테스트(비정상)
	 * 뿌릴 인원이 채팅방 인원수보다 클 경우
	 */
	@Test
	void rainMoneyErrTest() throws Exception {

		// given
		RequestDTO requestrainMoney = new RequestDTO();
		requestrainMoney.count = 300;
		requestrainMoney.total_amount = 500;

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(requestrainMoney);

		// When
		mockMvc.perform(
				post("/kakaopay/rainMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());

		// Then
		// TODO:
		// return E0108 Error

	}

	/**
	 * 뿌린 금액 받기 API 테스트(정상)
	 */
	@Test
	void receiveRainMoneyTest() throws Exception {
		// given
		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(receiverainMoney);

		// When
		mockMvc.perform(
				post("/kakaopay/receive")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "2").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());

		// Then
		// TODO:
		// return 201 Created
	}
	/**
	 * 뿌린 금액 받기 API 테스트(비정상)
	 * 1. 10분 유효 시간 초과
	 * 2. 본인이 뿌린 금액 받기 요청
	 * 3. 채팅방 권한 없는 사용자
	 * 4. 중복 받기 요청
	 */
	@Test
	void receiveRainMoneyErrTest() throws Exception {
		// given
		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(receiverainMoney);

		// When
		mockMvc.perform(
				post("/kakaopay/receive")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "2").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());

		// Then
		// TODO:
		// return E0107 Error
		// return E0104 Error
		// return E0302 Error
		// return E0101 Error
	}

	/*
	 * 뿌리기 정보 조회 API 테스트(정상)
	 */
	@Test
	void getRainMonerainMoneyDetailTest() throws Exception {
		// given
		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(receiverainMoney);

		// When
		mockMvc.perform(
				get("/kakaopay/rainMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());
	}
	/*
	 * 뿌리기 정보 조회 API 테스트(비정상)
	 * 1. 발급자 이외의 사용자 조회 요청
	 * 2. 7일 조희 기간 종료
	 */
	@Test
	void getRainMonerainMoneyDetailErrTest() throws Exception {
		// given
		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();

		// 결과값 JSON 형식으로 노출해줌
		Gson gson = new Gson();
		String resJson = gson.toJson(receiverainMoney);

		// When
		mockMvc.perform(
				get("/kakaopay/rainMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header(ROOMID, "AAA")
						.header(USERID, "4").content(objectMapper.writeValueAsString(resJson))
		).andDo(print());

		// Then
		// TODO:
		// return E0301 Error
		// return E0102 Error
	}
}
/** 뿌리기 요청에 사용되는 Body */
@DataJdbcTest
class RequestDTO {
	int total_amount;
	int count;
}
@DataJdbcTest
class ReceiveRequestDTO{
	String token;
}