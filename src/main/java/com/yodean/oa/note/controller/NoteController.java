package com.yodean.oa.note.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.note.entity.Note;
import com.yodean.oa.note.service.NoteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 3/27/18.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping
    public Result<Note> save(@RequestBody Note node) {
        return ResultUtil.success(noteService.save(node));
    }

    @GetMapping("/{id}")
    public Result<Note> findById(@PathVariable Integer id) {
        return ResultUtil.success(noteService.findById(id));
    }
}
