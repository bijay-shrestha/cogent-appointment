//package com.cogent.cogentappointment.admin.resource;
//
//import com.cogent.cogentappointment.admin.service.MinioFileService;
//import com.jlefebure.spring.boot.minio.MinioException;
//import io.minio.messages.Item;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLConnection;
//import java.util.List;
//
//import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
//import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.MinioFileConstants.BASE_FILE;
//import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.MinioFileConstants.FETCH_FILE;
//
///**
// * @author smriti ON 12/01/2020
// */
//@RestController
//@RequestMapping(API_V1 + BASE_FILE)
//public class MinioFileResource {
//
//    private final MinioFileService minioService;
//
//    public MinioFileResource(MinioFileService minioService) {
//        this.minioService = minioService;
//    }
//
//    @GetMapping
//    public List<Item> getAllList() throws MinioException {
//        return minioService.getAllList();
//    }
//
//    //FOR NO SUB-DIRECTORY CASE
//    @GetMapping("/{object}")
//    public InputStream getObject(@PathVariable("object") String object,
//                                 HttpServletResponse response)
//            throws IOException, MinioException {
//        return minioService.getObject(object);
//    }
//
//    //FOR SUB-DIRECTORY CASE
//    @GetMapping(FETCH_FILE)
//    public void getObjectWithSubDirectory(@PathVariable("subDirectory") String subDirectory,
//                                          @PathVariable("object") String object,
//                                          HttpServletResponse response)
//            throws IOException, MinioException {
//
//        InputStream inputStream = minioService.getObjectWithSubDirectory(subDirectory, object);
//
////         SET THE CONTENT TYPE AND ATTACHMENT HEADER.
//        response.addHeader("Content-disposition", "attachment;filename=" + object);
//        response.setContentType(URLConnection.guessContentTypeFromName(object));
//
//        // COPY THE STREAM TO THE RESPONSE'S OUTPUT STREAM.
//        IOUtils.copy(inputStream, response.getOutputStream());
//        response.flushBuffer();
//    }
//
//    @PostMapping("/{object}")
//    public void deleteFiles(@PathVariable("object") String object) throws MinioException {
//        minioService.deleteFiles(object);
//    }
//}
