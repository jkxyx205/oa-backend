package com.yodean.oa.meeting.dao;

import com.yodean.oa.meeting.entity.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 3/27/18.
 */
public interface MeetingUserRepository extends JpaRepository<MeetingUser, Integer> {
}
