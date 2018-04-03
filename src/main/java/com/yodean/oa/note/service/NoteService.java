package com.yodean.oa.note.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.note.dao.NoteRepository;
import com.yodean.oa.note.entity.Note;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
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
    private DocumentService documentService;

    @Resource
    private LabelService labelService;

    @Transactional
    public Integer save(Note note) {
        List<Label> labels = note.getLabels();

        noteRepository.save(note);

        labelService.save(Category.NOTE, note.getId(), labels);

        documentService.update(note.getDocIds(), Category.NOTE, note.getId());

        workspaceService.tipUsers(Category.NOTE, note.getId(), UserUtils.getUser().getId());

        return note.getId();
    }

    @Transactional
    public void update(Note note, Integer id) throws InvocationTargetException, IllegalAccessException {
        note.setId(id);
        noteRepository.updateNonNull(note);

        //便签更新的时候，不需要移动到inbox。因为是属于自己的
        //workspaceService.save(CategoryEnum.NOTE, note.getId(), UserUtils.getUser().getId());
    }

    public void delete(Integer ...id) {
        noteRepository.deleteLogical(id);
    }

    public Note findById(Integer id) {
        Optional<Note> optional = noteRepository.findById(id);
        if (optional.isPresent()) {
            Note note = optional.get();
            note.setLabels(labelService.findLabels(Category.NOTE, id));
            note.setDocuments(documentService.findById(Category.NOTE, id));

            return note;
        }

        throw new OANoSuchElementException();
    }
}
