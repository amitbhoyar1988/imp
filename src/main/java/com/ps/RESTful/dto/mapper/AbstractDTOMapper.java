package com.ps.RESTful.dto.mapper;

public interface AbstractDTOMapper<D,R,E> {

	public E dtoToEntity(D dto);
	
	public R entityToDto(E entity);
		
}
