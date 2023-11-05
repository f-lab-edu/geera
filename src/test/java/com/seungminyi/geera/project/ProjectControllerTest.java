package com.seungminyi.geera.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.seungminyi.geera.TestUtil;
import com.seungminyi.geera.exception.InsufficientPermissionException;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(projectService);
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 조회")
    public void testGetProject() throws Exception {
        List<Project> projects = TestUtil.createTestProjects();
        Mockito.when(projectService.getProjects(anyString(), anyString(), anyInt(), anyInt())).thenReturn(projects);

        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 생성")
    public void testCreateProject() throws Exception {
        Project createdProject = TestUtil.createTestProject();
        Mockito.when(projectService.createProject(any())).thenReturn(createdProject);

        mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                .contentType("application/json")
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 수정")
    public void testUpdateProject() throws Exception {
        Mockito.doNothing().when(projectService).updateProject(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/projects/1")
                .contentType("application/json")
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 수정 - 권한 없음")
    public void testUpdateProject_권한없음() throws Exception {
        InsufficientPermissionException exception = new InsufficientPermissionException("Forbidden");
        Mockito.doThrow(exception).when(projectService).updateProject(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/projects/1")
                .contentType("application/json")
                .content("{}"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 삭제")
    public void testDeleteProject() throws Exception {
        Mockito.doNothing().when(projectService).deleteProject(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/1"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 삭제 - 권한 없음")
    public void testDeleteProject_권한없음() throws Exception {
        InsufficientPermissionException exception = new InsufficientPermissionException("Forbidden");
        Mockito.doThrow(exception).when(projectService).deleteProject(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/1"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 맴버 초대")
    public void testAddProjectMember() throws Exception {
        Mockito.doNothing().when(projectService).addProjectMember(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 맴버 초대 - 권한없음")
    public void testAddProjectMember_권한없음() throws Exception {
        InsufficientPermissionException exception = new InsufficientPermissionException("Forbidden");
        Mockito.doThrow(exception).when(projectService).addProjectMember(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 맴버 초대 - 이미 초대된 맴버")
    public void testAddProjectMember_이미_초대된_맴버() throws Exception {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Conflict");
        Mockito.doThrow(exception).when(projectService).addProjectMember(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 맴버 삭제")
    public void testDeleteProjectMember() throws Exception {
        Mockito.when(projectService.deleteProjectMember(anyLong(), anyLong())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 맴버 삭제 - 권한없음")
    public void testDeleteProjectMember_권한없음() throws Exception {
        InsufficientPermissionException exception = new InsufficientPermissionException("Forbidden");
        Mockito.doThrow(exception).when(projectService).deleteProjectMember(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 멤버 삭제 - 본인삭제")
    public void testDeleteProjectMember_본인삭제() throws Exception {
        Mockito.when(projectService.deleteProjectMember(anyLong(), anyLong())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/1/members/2"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 초대 수락")
    public void testAcceptProjectInvitation() throws Exception {
        Mockito.doNothing().when(projectService).acceptProjectInvitation(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.patch("/projects/1/accept-invitation"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("프로젝트 초대 수락 - 초대받지 않음")
    public void testAcceptProjectInvitation_초대받지_않음() throws Exception {
        InsufficientPermissionException exception = new InsufficientPermissionException("Forbidden");
        Mockito.doThrow(exception).when(projectService).acceptProjectInvitation(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.patch("/projects/1/accept-invitation"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}