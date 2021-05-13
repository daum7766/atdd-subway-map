package wooteco.subway.line.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.UnitTest;
import wooteco.subway.exception.NotExistItemException;
import wooteco.subway.line.Line;
import wooteco.subway.line.LineDao;
import wooteco.subway.line.section.dto.SectionRequest;
import wooteco.subway.station.Station;
import wooteco.subway.station.StationDao;

@DisplayName("SectionService 관련 기능")
class SectionServiceTest extends UnitTest {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionService sectionService;
    private final Station gangnamStation = new Station(1L, "강남역");
    private final Station jamsilStation = new Station(2L, "잠실역");
    private final Station yeoksamStation = new Station(3L, "역삼역");
    private final Station sillimStation = new Station(4L, "신림역");
    private final Line line2 = new Line(1L, "2호선", "green");
    private final SectionRequest defaultSectionRequest = new SectionRequest(1L, 4L, 10);

    public SectionServiceTest(LineDao lineDao, StationDao stationDao,
        SectionService sectionService) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionService = sectionService;
    }

    @BeforeEach
    void setUp() {
        lineDao.save(line2);
        stationDao.save(gangnamStation);
        stationDao.save(jamsilStation);
        stationDao.save(yeoksamStation);
        stationDao.save(sillimStation);
    }

    @Test
    @DisplayName("구간을 저장 한다.")
    void save() {
        //given

        //when
        sectionService.save(1L, defaultSectionRequest, true);

        //then
        assertThat(sectionService.findByLineId(1L).getSections()).hasSize(1)
            .usingRecursiveComparison()
            .isEqualTo(Collections.singletonList(new Section(1L, 1L, 4L, 10)));
    }

    @Test
    @DisplayName("노선에 포함된 구간을 삭제한다")
    void deleteByLineId() {
        //given
        sectionService.save(1L, defaultSectionRequest, true);
        sectionService.save(1L, new SectionRequest(4L, 3L, 10), false);

        //when
        sectionService.deleteByLineId(1L);

        //then
        assertThatThrownBy(() -> sectionService.findByLineId(1L))
            .isInstanceOf(NotExistItemException.class);
    }

    @Test
    @DisplayName("노선에 포함된 구간을 가져온다")
    void findByLineId() {
        //given
        sectionService.save(1L, defaultSectionRequest, true);
        sectionService.save(1L, new SectionRequest(4L, 3L, 10), false);

        //when
        Sections sections = sectionService.findByLineId(1L);

        //then
        assertThat(sections.getSections()).hasSize(2)
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(
                new Section(1L, 1L, 4L, 10),
                new Section(2L, 4L, 3L, 10)
            ));
    }

    @Test
    @DisplayName("노선에 포함된 역을 삭제하면 구간은 합쳐진다.")
    void deleteByStationId() {
        //given
        sectionService.save(1L, defaultSectionRequest, true);
        sectionService.save(1L, new SectionRequest(4L, 3L, 10), false);

        //when
        sectionService.deleteByStationId(1L, 4L);

        //then
        Sections sections = sectionService.findByLineId(1L);
        assertThat(sections.getSections()).hasSize(1)
            .usingRecursiveComparison()
            .isEqualTo(Collections.singletonList(
                new Section(1L, 1L, 3L, 20)
            ));
    }
}