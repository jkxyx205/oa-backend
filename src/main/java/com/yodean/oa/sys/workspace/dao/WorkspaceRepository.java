package com.yodean.oa.sys.workspace.dao;

import com.yodean.oa.sys.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 3/27/18.
 */
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
}
