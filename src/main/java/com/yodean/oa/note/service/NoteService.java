package com.yodean.oa.note.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.note.dao.NoteRepository;
import com.yodean.oa.note.entity.Note;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by rick on 3/27/18.
 */
@Service
public class NoteService {

    @Resource
    private NoteRepository noteRepository;

    @Resource
    private DocumentService documentService;

    private void build(Note note) {
        List<Label> labels = note.getLabels();
        labels.forEach(label -> label.setLabelId(new Label.LabelId(Label.LabelCategory.NOTE, note.getId())));

        if (Objects.isNull(note.getId())) {//添加自身
            Workspace workspace = new Workspace();
            workspace.setAuthorityId(UserUtils.getUser().getId());
            workspace.setAuthorityType(Workspace.AuthorityType.USER);
            workspace.setCategory(Category.NOTE);
            note.getWorkspaces().add(workspace);

            note.getDocIds().forEach(docId -> {
                Document document = documentService.findById(docId);
                document.setCategory(DocumentCategory.NOTE);
                note.getDocuments().add(document);
            });
        }

        //

    }


    @Transactional
    public Integer save(Note note) {
        build(note);
        noteRepository.save(note);

//        documentService.update(note.getDocIds(), DocumentCategory.NOTE, note.getId());

        return note.getId();
    }

    /***
     * 附件的和标签的添加删除，可以用一个模块统一处理！！！！！
     */

    /**
     * 不级联更新（更新不包括标签和附件）
     * @param note
     * @param id
     */
    @Transactional
    public void update(Note note, Integer id) {
        note.setId(id);
        build(note);
        noteRepository.update(note);
    }

    public void delete(Integer ...id) {
        noteRepository.deleteLogical(id);
    }

    public Note findById(Integer id) {
        Optional<Note> optional = noteRepository.findById(id);
        if (optional.isPresent()) {
            Note note = optional.get();
            return note;
        }

        throw new OANoSuchElementException();
    }
}
