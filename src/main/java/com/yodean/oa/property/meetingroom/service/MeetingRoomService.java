package com.yodean.oa.property.meetingroom.service;

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.property.meetingroom.dao.MeetingRoomRepository;
import com.yodean.oa.property.meetingroom.entity.MeetingRoom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by rick on 5/16/18.
 */
@Service
public class MeetingRoomService {
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    /**
     * 新增会议室
     *
     * @param meetingRoom
     * @return
     */
    public MeetingRoom save(MeetingRoom meetingRoom) {
        meetingRoomRepository.save(meetingRoom);

        return meetingRoom;
    }

    /**
     * 编辑会议室
     *
     * @param meetingRoom
     * @return
     */
    public MeetingRoom update(MeetingRoom meetingRoom, int id) {
        meetingRoom.setId(id);
        meetingRoom = meetingRoomRepository.update(meetingRoom);
        return meetingRoom;
    }

    /**
     * 删除会议室室
     *
     * @param id
     */
    public Integer delete(int id) {
        return meetingRoomRepository.deleteLogical(id).size();
    }

    /**
     * 会议室详情
     *
     * @param id
     * @return
     */
    public MeetingRoom findById(int id) {
        Optional<MeetingRoom> optional = meetingRoomRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }

    /**
     * 根据关键字查询会议室
     *
     * @param kw
     * @param page
     * @param size
     * @return
     */
    public Page<MeetingRoom> list(String kw, int page, int size) {
        StringUtils.defaultIfBlank(kw, "");

        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "title"));


        return meetingRoomRepository.findAll((Specification<MeetingRoom>) (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title").as(String.class)), "%" + kw.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("equipment").as(String.class)), "%" + kw.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("address").as(String.class)), "%" + kw.toLowerCase() + "%")
        ), pageable);

    }
}
