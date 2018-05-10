package com.yodean.oa.meeting.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.meeting.dao.MeetingRepository;
import com.yodean.oa.meeting.entity.Meeting;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.workspace.entity.Workspace;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by rick on 3/27/18.
 */
@Service
public class MeetingService {

    @Resource
    private MeetingRepository meetingRepository;

    @Resource
    private WorkspaceService workspaceService;

    @Resource
    private DocumentService documentService;

    private void build(Meeting meeting) {

        List<Label> labels = meeting.getLabels();
        labels.forEach(label -> label.setLabelId(new Label.LabelId(Label.LabelCategory.MEETING, meeting.getId())));

        List<Workspace> mustWorkspaces =  meeting.getMustWorkspaces();
        mustWorkspaces.forEach(workspace -> {
            workspace.setCategoryId(meeting.getId());
            workspace.setCategory(Category.MEETING);
            workspace.setAuthorityType(Workspace.AuthorityType.USER);
        });

        List<Workspace> optionWorkspaces =  meeting.getOptionWorkspaces();
        optionWorkspaces.forEach(workspace -> {
            workspace.setCategoryId(meeting.getId());
            workspace.setCategory(Category.MEETING);
            workspace.setAuthorityType(Workspace.AuthorityType.USER);
            workspace.setUserType(Workspace.UserType.OPTIONAL);
        });

        //
        meeting.getDocIds().forEach(docId -> {
            Document document = documentService.findById(docId);
            document.setCategory(DocumentCategory.MEETING);
            meeting.getDocuments().add(document);
        });

    }

    @Transactional
    public Integer save(Meeting meeting) {
        build(meeting);
        meetingRepository.save(meeting);
        return meeting.getId();
    }

    @Transactional
    public void update(Meeting meeting, Integer id) {
        meeting.setId(id);
        build(meeting);
        meetingRepository.updateCascade(meeting);
        workspaceService.tip(Category.MEETING, id);
    }

    public Meeting findById(Integer id) {
        Optional<Meeting> optional = meetingRepository.findById(id);
        if (optional.isPresent()) {
            Meeting meeting = optional.get();
            return meeting;
        }

        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }

    /**
     * 取消会议
     * @param id
     */
    public void cancel(Integer id) {
        Meeting meeting = DataEntity.of(Meeting.class, id);
        meeting.setDelFlag(DataEntity.DEL_FLAG_REMOVE);

        meetingRepository.update(meeting);
        workspaceService.tip(Category.MEETING, id);
    }

    /**
     * 添加开会人
     */
    public void addUser(Integer id, Meeting meeting) {
        meeting.setId(id);
        build(meeting);
        meeting.getMustWorkspaces().addAll(meeting.getOptionWorkspaces());
        workspaceService.tip(Category.MEETING, id, meeting.getMustWorkspaces());
    }
}
