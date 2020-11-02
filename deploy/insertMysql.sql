-- 사용자 정보 4건 삽입
INSERT INTO `kakaopay`.`KP_TB_USER` VALUES (1, now(), 'login1'), (2, now(), 'login2'), (3, now(), 'login3'), (4, now(), 'login4');
-- 방 정보 3건 삽입
INSERT INTO `kakaopay`.`KP_TB_ROOM` VALUES ('AAA',  now(), '테스트방1'), ('BBB',  now(), '테스트방2'), ('CCC', now(), '테스트방3');
-- 채팅방 정보 5건 삽입
INSERT INTO `kakaopay`.`KP_TB_ROOM_USER` VALUES ('AAA', 1, now()), ('AAA', 2, now()), ('AAA',3,  now()),( 'BBB', 2, now()), ('BBB',4,  now());
-- Let's Start!!
