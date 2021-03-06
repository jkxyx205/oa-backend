package com.yodean.oa.notice.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.notice.dao.NoticeRepository;
import com.yodean.oa.notice.entity.Notice;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by rick on 5/4/18.
 */
@Service
public class NoticeService {
    @Resource
    private NoticeRepository noticeRepository;

    @Resource
    private DocumentService documentService;

    private void build(Notice notice) {
        List<Label> labels = notice.getLabels();
        labels.forEach(label -> label.setLabelId(new Label.LabelId(Label.LabelCategory.NOTICE, notice.getId())));

        List<Workspace> workspaces =  notice.getWorkspaces();
        workspaces.forEach(workspace -> workspace.setCategory(Category.NOTICE));

        //
        notice.getDocIds().forEach(docId -> {
            Document document = documentService.findById(docId);
            document.setCategoryId(notice.getId());
            document.setCategory(DocumentCategory.NOTICE);
            notice.getDocuments().add(document);
        });

    }


    @Transactional
    public Integer save(Notice notice) {
        build(notice);
        noticeRepository.save(notice);
        return notice.getId();
    }


    @Transactional
    public void update(Notice notice, Integer id) {
        notice.setId(id);
        build(notice);
        noticeRepository.updateCascade(notice);

    }

    public void delete(Integer ...ids) {
        noticeRepository.deleteLogical(ids);
    }

    public Notice findById(Integer id) {
        Optional<Notice> optional = noticeRepository.findById(id);
        if (optional.isPresent()) {
            Notice notice = optional.get();
            return notice;
        }
        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }

    /**
     * 获取当前用户所有可以读取的通知公告
     * @return
     */
//    public Page<Notice> findAll(String title) {
//
//        Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "updateDate"));
//
//
//        Notice notice = new Notice();
//        notice.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
//        notice.setTitle(title);
//
//        List<NoticeAuthority> noticeAuthorityList = new ArrayList<>();
//
//        noticeAuthorityList.add(NoticeAuthority.of("1000", UserUtils.getUser().getId() + ""));
//
//        Integer orgIds[] = UserUtils.getUser().getOrgIds();
//        for (Integer orgId : orgIds) {
//            noticeAuthorityList.add(NoticeAuthority.of("0", orgId + ""));
//        }
//        notice.setNoticeAuthorityList(noticeAuthorityList);
//
//        //创建匹配器，即如何使用查询条件
//        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
////                .withMatcher("noticeAuthorityList",)
//                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains()); //名称采用“模糊”的方式查询
//
//
//        Example example = Example.of(notice, matcher);
//
//        return noticeRepository.findAll(example, pageable);
//
//    }

//    public Page<Notice> findAll2(String title) {
//        Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.DESC, "updateDate"));
//        return noticeRepository.findAll(new NoticeSpecification(title), pageable);
//    }


//    private class NoticeSpecification implements Specification<Notice> {
//
//        private String title;
//
//        NoticeSpecification(String title) {
//            this.title = title;
//        }
//
//        @Override
//        public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//            List<Predicate> list = new ArrayList<>();
//
//            if (Objects.nonNull(title))
//                list.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
//
//            list.add(criteriaBuilder.equal(root.get("delFlag"), DataEntity.DEL_FLAG_NORMAL));
//
//            Join<Notice, NoticeAuthority> join = root.join("noticeAuthorityList", JoinType.LEFT);
//
//            Predicate p1 = criteriaBuilder.and(criteriaBuilder.equal(join.get("authorityType"), 0),
//                    criteriaBuilder.and(criteriaBuilder.equal(join.get("authorityId"), UserUtils.getUser().getId())));
//
//            CriteriaBuilder.In<Object> in = criteriaBuilder.in(join.get("authorityId"));
//
//            Predicate p2 = criteriaBuilder.and(criteriaBuilder.equal(join.get("authorityType"), 1),
//                    criteriaBuilder.and(in));
//            for (int orgId : UserUtils.getUser().getOrgIds()) {
//                in.value(orgId);
//            }
//
//            Predicate p3 = criteriaBuilder.or(p1,p2);
//
//            list.add(p3);
//
//            Predicate[] p = new Predicate[list.size()];
//            return criteriaBuilder.and(list.toArray(p));
//        }
//    }

}

