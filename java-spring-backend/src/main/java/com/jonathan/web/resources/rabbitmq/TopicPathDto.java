package com.jonathan.web.resources.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicPathDto 
{
	private String username; 
	private String vhost; 
	private String resource;
	private String name;
	private String permission;
	private String routing_key;
}

