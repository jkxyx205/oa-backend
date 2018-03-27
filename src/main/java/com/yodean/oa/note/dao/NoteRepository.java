package com.yodean.oa.note.dao;

import com.yodean.oa.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 3/27/18.
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {

}
