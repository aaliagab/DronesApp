package com.musalasoft.dronesapp.services.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	public Resource upload(String imageName) throws MalformedURLException;
	public String copy(MultipartFile file, Long id) throws IOException;
	public boolean delete(String imageName);
	public Path path(String imageName);
}
