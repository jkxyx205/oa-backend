package com.yodean.oa.meeting.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.meeting.dao.MeetingRepository;
import com.yodean.oa.meeting.entity.Meeting;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
    private LabelService labelService;

    @Resource
    private DocumentService documentService;

    @Resource
    private BaseService baseService;

    @Transactional
    public Integer save(Meeting meeting) {
        meetingRepository.save(meeting);

//        workspaceService.tipUsers(Category.MEETING, meeting.getId(), meeting.getUserMap());

        //add Label
        labelService.save(Label.LabelCategory.MEETING, meeting.getId(), meeting.getLabels());

        //add document
        documentService.update(meeting.getDocIds(), DocumentCategory.MEETING, meeting.getId());

        return meeting.getId();
    }

    @Transactional
    public void update(Meeting meeting, Integer id) {
        meeting.setId(id);
//        meetingRepository.updateNonNull(meeting);
//        workspaceService.tipAll(Category.TASK, meeting.getId());
    }

    public Meeting findById(Integer id) {
        Optional<Meeting> optional = meetingRepository.findById(id);
        if (optional.isPresent()) {
            Meeting meeting = optional.get();
//            meeting.setUsers(workspaceService.findUsers(Category.MEETING, id));
            meeting.setLabels(labelService.findLabels(Label.LabelCategory.MEETING, id));
            meeting.setDocuments(documentService.findById(DocumentCategory.MEETING, id));
            return meeting;
        }

        throw new OANoSuchElementException();
    }

    public void cancel(Integer id) {
        Meeting meeting = findById(id);
        meeting.setDelFlag(DataEntity.DEL_FLAG_REMOVE);
        meetingRepository.save(meeting);
    }
}
