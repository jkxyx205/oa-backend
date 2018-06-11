package com.yodean.oa.sys.org.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.plugin.ztree.dto.TreeNode;
import com.yodean.oa.common.plugin.ztree.service.ZtreeService;
import com.yodean.oa.common.util.ResultUtils;
import com.yodean.oa.sys.org.entity.Organization;
import com.yodean.oa.sys.org.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2018/3/23.
 */
@RestController
@RequestMapping("/orgs")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ZtreeService ztreeService;

    @PostMapping
    public Result saveOrganization(@RequestBody Organization organization) {
        return ResultUtils.success(organizationService.save(organization));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Organization organization) {
        organizationService.update(organization, id);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public Result<Organization> findById(@PathVariable Integer id) {
        return ResultUtils.success(organizationService.findById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Organization> delete(@PathVariable Integer id) {
        organizationService.delete(id);
        return ResultUtils.success();
    }


    @GetMapping
    public Result<List<Organization>> getOrgsAsList() {
        List<Organization> list = organizationService.findAll();
        return ResultUtils.success(list);
    }

    @GetMapping("/tree")
    public List<TreeNode> getOrgsAsTree(@RequestParam Map<String, Object> params) {
        List<TreeNode> list = ztreeService.getTreeNode("SELECT\n" +
                "\tname,\n" +
                "\tparent_id AS pId,\n" +
                "\tid,\n" +
                "\tcase when parent_id is null then 'true' else 'false' end as open\n" +
//                "\t'true' as parent\n" +
                "FROM\n" +
                "\tsys_org\n" +
                "WHERE\n" +
                "\tNAME LIKE :name AND del_flag = '1'", params);
        return list;
    }

    @GetMapping("/tree/users")
    public List<TreeNode> getUsersAsTree(@RequestParam Map<String, Object> params) {
        params.put("orgName", params.get("name"));

        List<TreeNode> list = ztreeService.getTreeNode("SELECT\n" +
                "\tname,\n" +
                "\tCONCAT('v-',parent_id) AS pId,\n" +
                "\tCONCAT('v-',id) id,\n" +
                "\tcase when parent_id is null then 'true' else 'false' end as open\n" +
                "FROM\n" +
                "\tsys_org\n" +
                "WHERE\n" +
                "\tNAME LIKE :name\n" +
                "AND del_flag ='1'\n" +
                "union all\n" +
                "SELECT\n" +
                "\tCONCAT(chinese_name,IF(g.manager_id is null,'','[主管]')) AS name,\n" +
                "\tCONCAT('v-',r.org_id) AS pId,\n" +
                "\tu.id,\n" +
                "\t'false' AS open\n" +
                "FROM\n" +
                "\tsys_user u\n" +
                "LEFT JOIN sys_user_org r ON r.user_id = u.id\n" +
                "LEFT JOIN sys_org g ON g.id = r.org_id AND g.manager_id = u.id\n" +
                "WHERE\n" +
                "u.chinese_name LIKE :orgName\n" +
                "AND\tu.del_flag = '1'", params);
        return list;
    }

}
