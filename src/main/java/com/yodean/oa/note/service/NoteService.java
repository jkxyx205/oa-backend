package com.yodean.oa.note.service;

import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.note.dao.NoteRepository;
import com.yodean.oa.note.entity.Note;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import com.yodean.oa.sys.util.UserUtils;
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
public class NoteService {

    @Resource
    private NoteRepository noteRepository;

    @Resource
    private WorkspaceService workspaceService;

    @Resource
    private LabelService labelService;

    @Transactional
    public Note save(Note note) {
        List<Label> labels = note.getLabels();

        note = noteRepository.save(note);

        labelService.save(CategoryEnum.NOTE, note.getId(), labels);

        workspaceService.save(CategoryEnum.TASK, note.getId(), UserUtils.getUser().getId());

        note.setLabels(labels);
        return note;
    }

    public Note findById(Integer id) {
        Optional<Note> optional = noteRepository.findById(id);
        if (optional.isPresent()) {
            Note note = optional.get();
            note.setLabels(labelService.findLabels(CategoryEnum.NOTE, id));
            return note;
        }

        throw new OANoSuchElementException();
    }
}
