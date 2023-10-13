package lache.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ErrorInputData.class)
    public ResponseEntity<ErrorInputData> eidHandler(ErrorInputData e) {
        return new ResponseEntity<ErrorInputData>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<UnauthorizedError> ueHandler(UnauthorizedError e) {
        return new ResponseEntity<UnauthorizedError>(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ErrorDeleteFile.class)
    public ResponseEntity<ErrorDeleteFile> edfHandler(ErrorDeleteFile e) {
        return new ResponseEntity<ErrorDeleteFile>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorUploadFile.class)
    public ResponseEntity<ErrorUploadFile> eufHandler(ErrorUploadFile e) {
        return new ResponseEntity<ErrorUploadFile>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorGettingFileList.class)
    public ResponseEntity<ErrorGettingFileList> egflHandler(ErrorGettingFileList e) {
        return new ResponseEntity<ErrorGettingFileList>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
