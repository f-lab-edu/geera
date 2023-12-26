package com.seungminyi.geera.issue;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class IssueRequestFixture {
	private ObjectMapper objectMapper = new ObjectMapper();
	public Long projectId =  1L;
	public String issueType = "EPIC";
	public String issueStatus = "TODO";
	public String issueDescription = "이슈 요약";
	public String issueDetail = "이슈 설명";
	public Long issueContractId = 1L;
	public Date createAt = null;
	public Long topIssue = null;

	public String toJson() throws JsonProcessingException {
		return objectMapper.writeValueAsString(this);
	}
}
