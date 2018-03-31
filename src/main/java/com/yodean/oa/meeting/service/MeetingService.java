package com.yodean.oa.meeting.service;

import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.meeting.dao.MeetingRepository;
import com.yodean.oa.meeting.dao.MeetingUserRepository;
import com.yodean.oa.meeting.entity.Meeting;
import com.yodean.oa.meeting.entity.MeetingUser;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by rick on 3/27/18.
 */
@Service
public class MeetingService {

    @Resource
    private MeetingUserRepository meetingUserRepository;

    @Resource
    private MeetingRepository meetingRepository;

    @Resource
    private WorkspaceService workspaceService;

    @Resource
    private BaseService baseService;

    @Transactional
    public Meeting save(Meeting meeting) {
        //添加会议内容
        Meeting persit = meetingRepository.save(meeting);
        meeting.setId(persit.getId());

        int len = meeting.getCcUserIds().size() + meeting.getToUserIds().size();

        List<MeetingUser> meetingUserList = new ArrayList<>(len);
        Set<Integer> userIds = new HashSet<>(len);

        meeting.getToUserIds().forEach(userId -> {
            MeetingUser meetingUser = new MeetingUser(meeting.getId(), userId, true);
            meetingUserList.add(meetingUser);
            userIds.add(userId);
        });

        meeting.getCcUserIds().forEach(userId -> {
            if (!userIds.contains(userId)) {
                MeetingUser meetingUser = new MeetingUser(meeting.getId(), userId, false);

                meetingUserList.add(meetingUser);
                userIds.add(userId);
            }
        });
        //添加参会人员
        meetingUserRepository.saveAll(meetingUserList);

        //
        workspaceService.tipUsers(CategoryEnum.MEETING, meeting.getId(), userIds);

        return meeting;
    }

    public Meeting findById(Integer id) {
        Optional<Meeting> optional = meetingRepository.findById(id);
        if (optional.isPresent()) {
            Meeting meeting = optional.get();
            meeting.setToUsers(getMeetingUser(id, true));
            meeting.setCcUsers(getMeetingUser(id, false));
            return meeting;
        }

        throw new OANoSuchElementException();
    }

    /***
     * 获取参会人员
     * @param meetingId
     * @param to
     * @return
     */
    private List<User> getMeetingUser(Integer meetingId, Boolean to) {
        String sql = "SELECT\n" +
                "\tid,\n" +
                "\tchinese_name chineseName\n" +
                "FROM\n" +
                "\tsys_user su\n" +
                "WHERE\n" +
                "\tEXISTS (\n" +
                "\t\tSELECT\n" +
                "\t\t\t1\n" +
                "\t\tFROM\n" +
                "\t\t\tt_meeting_user smu\n" +
                "\t\tWHERE\n" +
                "\t\t\tsmu.meeting_id = :meetingId\n" +
                "\t\tAND smu.is_to = :to\n" +
                "\t\tAND su.id = smu.user_id\n" +
                "\t)";

        Map<String,Object> params = new HashMap<>(2);
        params.put("meetingId", meetingId);
        params.put("to", to ? 1: 0);
        return baseService.query(sql, params, User.class);
    }
}
