package wooteco.subway;

import java.util.Objects;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.exception.SubwayException;

public class StringInput {

    private final String item;

    public StringInput(String item) {
        validate(item);
        this.item = item;
    }

    private void validate(String item) {
        if (Objects.isNull(item) || item.trim().length() == 0) {
            throw new SubwayCustomException(SubwayException.INVALID_INPUT_NAME_OR_COLOR_EXCEPTION);
        }
    }

    public String getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringInput that = (StringInput) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
