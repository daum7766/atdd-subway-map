package wooteco.subway.exception;

public class NotExistItemException extends RuntimeException {

    private static final String MESSAGE = "[ERROR] 해당 아이템이 존재하지 않습니다.";

    public NotExistItemException() {
        super(MESSAGE);
    }
}
