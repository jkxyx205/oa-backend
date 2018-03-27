package com.yodean.oa.meeting.dao;

import com.yodean.oa.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 3/27/18.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
}
