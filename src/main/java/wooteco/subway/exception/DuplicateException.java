package wooteco.subway.exception;

public class DuplicateException extends RuntimeException {

    private static final String MESSAGE = "[ERROR] 중복된 이름입니다.";

    public DuplicateException() {
        super(MESSAGE);
    }
}
