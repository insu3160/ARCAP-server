package com.yiu.arcap.service;

import com.yiu.arcap.constant.Direction;
import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.dto.CapsuleRequest.CreateDTO;
import com.yiu.arcap.dto.CapsuleRequest.LocationDto;
import com.yiu.arcap.dto.CapsuleResponseDto;
import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.Location;
import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.entity.UserParty;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.CapsuleRepository;
import com.yiu.arcap.repository.PartyRepository;
import com.yiu.arcap.repository.UserPartyRepository;
import com.yiu.arcap.repository.UserRepository;
import com.yiu.arcap.util.GeometryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.Point;

@Service
@RequiredArgsConstructor
public class CapsuleService {
    private final UserRepository userRepository;

    private final PartyRepository partyRepository;

    private final UserPartyRepository userPartyRepository;

    private final CapsuleRepository capsuleRepository;
    @PersistenceContext
    private EntityManager em;
    @Transactional
    public Boolean create(String email, CreateDTO request) throws ParseException {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(request.getPid())
                .orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));
        if (request.getLatitude() == null || request.getLongitude() == null){
            throw new CustomException(ErrorCode.NOT_EXIST);
        }
        Point point = (Point) new WKTReader().read(String.format("POINT(%s %s)", request.getLatitude(), request.getLongitude()));
        if (userPartyRepository.existsByStatusAndUserAndParty(ParticipationStatus.ACCEPTED, user, party)){
            try {
                Capsule capsule = Capsule.builder()
                        .user(user)
                        .party(party)
                        .title(request.getTitle())
                        .contents(request.getContents())
                        .locationName(request.getLocationName())
                        .likesCount(0)
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .point(point)
                        .build();
                capsuleRepository.save(capsule);
                return true;
            }catch (Exception e){
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public List<CapsuleResponseDto> getPartyCapsules(String email, LocationDto request) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 사용자가 참여하고 있는 파티의 ID를 조회
        List<UserParty> userParties = userPartyRepository.findByStatusAndUser(ParticipationStatus.ACCEPTED, user);
        if (userParties.isEmpty()) {
            throw new CustomException(ErrorCode.NO_AUTH);
        }
        List<Long> partyIds = userParties.stream().map(up -> up.getParty().getPid()).collect(Collectors.toList());

        // Location 자료형으로 변수를 선언하여 해당 요청받은 x,y 값으로 북동쪽과 남서쪽의 위치를 계산 2KM
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        Location northEast = GeometryUtil.calculate(latitude, longitude, 2.0, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(latitude, longitude, 2.0, Direction.SOUTHWEST.getBearing());

        String join = partyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        // 네이티브 쿼리 사용
        // 네이티브 쿼리 수정
        String nativeQuery = String.format(
                "SELECT cid FROM capsule c " +
                        "WHERE c.pid IN (%s) " +
                        "AND MBRContains(ST_LINESTRINGFROMTEXT('LINESTRING(%f %f, %f %f)'), c.point) = 1",
                join,
                northEast.getLatitude(), northEast.getLongitude(), southWest.getLatitude(), southWest.getLongitude()
        );

        @SuppressWarnings("unchecked")
        List<Long> capsuleIds = em.createNativeQuery(nativeQuery, Long.class).getResultList();

        // capsuleIds를 사용하여 Capsule 엔티티를 조회
        List<Capsule> capsules = capsuleRepository.findAllById(capsuleIds);

        return capsules.stream()
                .map(capsule -> CapsuleResponseDto.builder()
                        .cid(capsule.getCid())
                        .title(capsule.getTitle())
                        .contents(capsule.getContents())
                        .locationName(capsule.getLocationName())
                        .latitude(capsule.getLatitude())
                        .longitude(capsule.getLongitude())
                        .partyName(capsule.getParty().getPartyName())
                        .nickName(capsule.getUser().getNickname())
                        .createdAt(capsule.getCreatedAt())
                        .likesCount(capsule.getLikesCount())
                        .build())
                .collect(Collectors.toList());
    }

}
