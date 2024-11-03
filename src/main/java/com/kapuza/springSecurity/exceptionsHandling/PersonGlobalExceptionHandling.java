package com.kapuza.springSecurity.exceptionsHandling;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonGlobalExceptionHandling {

    /**Этот метод отвечает за обработку исключений.
     * ResponseEntity - это возвращаемый тип этого метода.
     * В джинерике, которого будет Тип объекта, который добавляет в HTTP ответ тело с ошибкой для вывода в "json" формате.
     * В нашем случае тип-PersonIncorrectData. А добавляется он тогда когда выбрасывается Exception указанный в параметре этого метода
     */
    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(NotFoundUserException notFoundUserException){
        UserIncorrectData userIncorrectData =new UserIncorrectData();
        userIncorrectData.setInfo(notFoundUserException.getMessage());
        return new ResponseEntity<>(userIncorrectData, HttpStatus.NOT_FOUND);
    }

    /**Тоже что и выше метод, только тут, если передали не просто не существующий ID, а любой другой символ отличный от цифры
     */
    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(NumberFormatException notFoundUserException){
        UserIncorrectData userIncorrectData =new UserIncorrectData();
        userIncorrectData.setInfo(notFoundUserException.getMessage()+". ID is not digit");
        return new ResponseEntity<>(userIncorrectData, HttpStatus.BAD_REQUEST);
    }


    /**Тоже что и выше метод, только тут, ошибки при валидации
     */
    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(UserValidationException exception){
        UserIncorrectData userIncorrectData =new UserIncorrectData();
        userIncorrectData.setInfo(exception.getMessage());
        return new ResponseEntity<>(userIncorrectData, HttpStatus.BAD_REQUEST);
    }

    /**Все RuntimeException
     */
    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(RuntimeException exception){
        UserIncorrectData userIncorrectData =new UserIncorrectData();
        userIncorrectData.setInfo(exception.getMessage());
        return new ResponseEntity<>(userIncorrectData, HttpStatus.BAD_REQUEST);
    }



}
