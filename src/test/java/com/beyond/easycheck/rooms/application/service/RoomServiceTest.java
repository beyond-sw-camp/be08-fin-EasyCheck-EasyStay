package com.beyond.easycheck.rooms.application.service;

import com.beyond.easycheck.accomodations.infrastructure.entity.AccommodationEntity;
import com.beyond.easycheck.accomodations.infrastructure.repository.AccommodationRepository;
import com.beyond.easycheck.common.exception.EasyCheckException;
import com.beyond.easycheck.rooms.infrastructure.entity.RoomEntity;
import com.beyond.easycheck.rooms.infrastructure.entity.RoomStatus;
import com.beyond.easycheck.rooms.infrastructure.repository.RoomRepository;
import com.beyond.easycheck.rooms.ui.requestbody.RoomCreateRequest;
import com.beyond.easycheck.rooms.ui.requestbody.RoomUpdateRequest;
import com.beyond.easycheck.rooms.ui.view.RoomView;
import com.beyond.easycheck.roomtypes.infrastructure.entity.RoomtypeEntity;
import com.beyond.easycheck.roomtypes.infrastructure.repository.RoomtypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.beyond.easycheck.accomodations.exception.AccommodationMessageType.ACCOMMODATION_NOT_FOUND;
import static com.beyond.easycheck.rooms.exception.RoomMessageType.*;
import static com.beyond.easycheck.roomtypes.exception.RoomtypeMessageType.ROOM_TYPE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomtypeRepository roomtypeRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private RoomService roomService;

    @InjectMocks
    private RoomtypeEntity roomType;

    @InjectMocks
    private AccommodationEntity accommodationEntity;

    @Test
    @DisplayName("객실 생성 - 성공")
    void createRoom_success() {
        // Given
        when(roomtypeRepository.findById(1L)).thenReturn(Optional.of(roomType));

        RoomCreateRequest roomCreateRequest = new RoomCreateRequest(
                1L,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        RoomEntity roomEntity = new RoomEntity(
                1L,
                roomType,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        when(roomRepository.save(any(RoomEntity.class))).thenReturn(roomEntity);

        // When & Then
        assertThatCode(() -> roomService.createRoom(roomCreateRequest))
                .doesNotThrowAnyException();

        // Verify
        verify(roomRepository).save(any(RoomEntity.class));
    }

    @Test
    @DisplayName("객실 생성 실패 - 존재하지 않는 roomtypeID")
    void createRoom_fail() {
        // Given
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest(
                10000L,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        // When & Then
        assertThatThrownBy(() -> roomService.createRoom(roomCreateRequest))
                .isInstanceOf(EasyCheckException.class)
                .hasMessage(ROOM_TYPE_NOT_FOUND.getMessage());
        // Verify
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    @DisplayName("객실 단일 조회 성공")
    void readRoom_success() {
        // Given
//        accommodationEntity = accommodationRepository.findById(1L)
//                .orElseThrow(() -> new EasyCheckException(ACCOMMODATION_NOT_FOUND));
//        roomType = roomtypeRepository.findById(1L)
//                .orElseThrow(() -> new EasyCheckException(ROOM_TYPE_NOT_FOUND));

        when(accommodationRepository.findById(roomType.getAccommodationEntity().getId())).thenReturn(Optional.of(accommodationEntity));
        when(roomtypeRepository.findById(1L)).thenReturn(Optional.of(roomType));

        RoomEntity roomEntity = new RoomEntity(
                1L,
                roomType,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        when(roomRepository.findById(1L)).thenReturn(Optional.of(roomEntity));

        RoomView roomView = new RoomView(
                1L,
                "402",
                "roomPic1",
                10,
                RoomStatus.예약가능,
                roomType.getRoomTypeId(),
                roomType.getAccommodationEntity().getId(),
                roomType.getDescription(),
                roomType.getTypeName(),
                roomType.getMaxOccupancy()
        );

        // When
        RoomView readRoom = roomService.readRoom(1L);

        // Then
        assertThat(readRoom).isEqualTo(roomView);
    }

    @Test
    @DisplayName("객실 단일 조회 실패 - 존재하지 않는 roomID")
    void readRoom_fail() {
        // Given
        Long roomId = 999L;

        when(accommodationRepository.findById(1L)).thenReturn(Optional.of(accommodationEntity));
        when(roomtypeRepository.findById(1L)).thenReturn(Optional.of(roomType));
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> roomService.readRoom(roomId))
                .isInstanceOf(EasyCheckException.class)
                .hasMessage(ROOM_NOT_FOUND.getMessage());

        verify(roomRepository).findById(roomId);

    }

    @Test
    @DisplayName("객실 전체 조회 성공")
    void readRooms_success() {

    }

    @Test
    @DisplayName("객실 전체 조회 실패")
    void readRooms_fail() {
        // Given
        when(roomRepository.findAll()).thenThrow(new EasyCheckException(ROOMS_NOT_FOUND));

        // When & Then
        assertThatThrownBy(() -> roomService.readRooms())
                .isInstanceOf(EasyCheckException.class)
                .hasMessage(ROOMS_NOT_FOUND.getMessage());

        // Verify
        verify(roomRepository).findAll();
    }

    @Test
    @DisplayName("객실 정보 수정 성공")
    void updateRoom_success() {
        // Given
        RoomEntity existingRoom = new RoomEntity(
                1L,
                roomType,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        RoomUpdateRequest updateRoom = new RoomUpdateRequest(
                "403",
                "roomPic2",
                5,
                RoomStatus.예약불가
        );

        // 기존 객실 조회
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));

        // When
        roomService.updateRoom(1L, updateRoom);

        // Then
        assertThat(existingRoom.getRoomNumber()).isEqualTo("403");
        assertThat(existingRoom.getRoomPic()).isEqualTo("roomPic2");
        assertThat(existingRoom.getStatus()).isEqualTo(RoomStatus.예약불가);
        assertThat(existingRoom.getRoomAmount()).isEqualTo(5);

        // Verify
        verify(roomRepository).findById(1L);
    }

    @Test
    @DisplayName("객실 정보 수정 실패 - 잘못된 입력값")
    void updateRoom_fail() {
        // Given
        RoomEntity existingRoom = new RoomEntity(
                1L,
                roomType,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        RoomUpdateRequest updateRequest = new RoomUpdateRequest(
                "403",
                "roomPic2",
                -5,
                RoomStatus.예약불가
        );

        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));

        // When & Then
        assertThatThrownBy(() -> roomService.updateRoom(1L, updateRequest))
                .isInstanceOf(EasyCheckException.class)
                .hasMessage(ARGUMENT_NOT_VALID.getMessage());

        // Verify
        verify(roomRepository).findById(1L);
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    @DisplayName("객실 정보 삭제 성공")
    void deleteRoom_success() {
        // Given
        Long roomId = 1L;

        RoomEntity roomEntity = new RoomEntity(
                roomId,
                roomType,
                "402",
                "roomPic1",
                RoomStatus.예약가능,
                10
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(roomEntity));

        // When
        roomService.deleteRoom(roomId);

        // Then
        verify(roomRepository).delete(roomEntity);
    }

    @Test
    @DisplayName("객실 정보 삭제 실패 - 잘못된 RoomID")
    void deleteRoom_fail() {
        Long roomId = 999L;

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> roomService.deleteRoom(roomId))
                .isInstanceOf(EasyCheckException.class)
                .hasMessage(ROOM_NOT_FOUND.getMessage());

        verify(roomRepository).findById(roomId);
    }

}

