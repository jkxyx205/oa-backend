package com.yodean.oa.property.meetingroom.service;

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.property.meetingroom.dao.MeetingRoomRepository;
import com.yodean.oa.property.meetingroom.entity.MeetingRoom;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param meetingRoom
     * @return
     */
    public MeetingRoom save(MeetingRoom meetingRoom) {
        meetingRoomRepository.save(meetingRoom);

        return meetingRoom;
    }

    /**
     * 编辑会议室
     * @param meetingRoom
     * @return
     */
    public MeetingRoom update(MeetingRoom meetingRoom) {
        meetingRoomRepository.update(meetingRoom);
        return meetingRoom;
    }

    /**
     * 删除会议室室
     * @param id
     */
    public void delete(Integer id) {
        meetingRoomRepository.deleteLogical(id);
    }

    /**
     * 会议室详情
     * @param id
     * @return
     */
    public MeetingRoom findById(Integer id) {
        Optional<MeetingRoom> optional = meetingRoomRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }
}
