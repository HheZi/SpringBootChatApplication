package com.chat_app.service.mapper;

public interface ModelMapper<E, R, W> {
	E writeDtoToModel(W dto);
	
	R modelToReadDto(E entity);
}
