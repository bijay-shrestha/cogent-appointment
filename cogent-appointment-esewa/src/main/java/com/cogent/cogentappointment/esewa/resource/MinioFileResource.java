//package com.cogent.cogentappointment.esewa.resource;
//
//import com.cogent.cogentappointment.esewa.service.MinioFileService;
//import com.jlefebure.spring.boot.minio.MinioException;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLConnection;
//
//import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.MinioFileConstant.BASE_API_VALUE;
//import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.MinioFileConstant.FETCH_FILE_OPERATION;
//import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
//import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.MinioFileConstants.BASE_FILE;
//import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.MinioFileConstants.FETCH_FILE;
//
///**
// * @author smriti ON 12/01/2020
// */
//@RestController
//@RequestMapping(API_V1 + BASE_FILE)
//@Api(BASE_API_VALUE)
//public class MinioFileResource {
//
//    private final MinioFileService minioService;
//
//    public MinioFileResource(MinioFileService minioService) {
//        this.minioService = minioService;
//    }
//
//    @GetMapping(FETCH_FILE)
//    @ApiOperation(FETCH_FILE_OPERATION)
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
//}
