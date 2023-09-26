package com.musalasoft.dronesapp.services.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String DIR_UPLOAD = "uploads";

	@Override
	public Resource upload(String imageName) throws MalformedURLException {
		Path path = path(imageName);
		log.info(path.toString());
		Resource resource = null;
		resource = new UrlResource(path.toUri());
		if(!resource.exists() || !resource.isReadable()) {
			path = Paths.get("src/main/resources/static/images").resolve("imageNone.png").toAbsolutePath();
			resource = new UrlResource(path.toUri());
			log.error("Could not load image "+imageName);
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file, Long id) throws IOException {
		String fileName = id+"_"+file.getOriginalFilename().replace(" ", "");
		Path path = path(fileName);
		log.info(path.toString());
		Files.copy(file.getInputStream(), path);
		return fileName;
	}

	@Override
	public boolean delete(String imageName) {
		if(imageName!=null && !imageName.equals("")) {
			Path path_delete = path(imageName);
			File fileBefore = path_delete.toFile();
			if(fileBefore.exists() && fileBefore.canRead()) {
				fileBefore.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path path(String imageName) {
		return Paths.get(DIR_UPLOAD).resolve(imageName).toAbsolutePath();
	}

}
