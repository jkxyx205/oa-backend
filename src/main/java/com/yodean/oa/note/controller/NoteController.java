package com.yodean.oa.note.controller;

import com.yodean.oa.common.dto.IdsDto;
import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtils;
import com.yodean.oa.note.entity.Note;
import com.yodean.oa.note.service.NoteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by rick on 3/27/18.
 */
@RestController
@RequestMapping("/notes")
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping
    public Result<Integer> save(@RequestBody Note node) {
        return ResultUtils.success(noteService.save(node));
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Note node, @PathVariable Integer id) throws InvocationTargetException, IllegalAccessException {
        noteService.update(node, id);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public Result<Note> findById(@PathVariable Integer id) {
        return ResultUtils.success(noteService.findById(id));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        noteService.delete(id);
        return ResultUtils.success();
    }

    /***
     *
     * @param idsDto
     * @return
     */
    @DeleteMapping
    public Result batchDelete(@RequestBody IdsDto idsDto) {
        noteService.delete(idsDto.getIds());
        return ResultUtils.success();
    }
}
