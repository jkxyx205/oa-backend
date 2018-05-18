package com.yodean.oa.property.meetingroom.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.property.meetingroom.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by rick on 5/16/18.
 */
public interface MeetingRoomRepository extends ExtendedRepository<MeetingRoom, Integer>, JpaSpecificationExecutor<MeetingRoom> {

}
