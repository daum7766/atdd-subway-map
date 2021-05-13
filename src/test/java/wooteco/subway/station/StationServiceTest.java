package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.UnitTest;
import wooteco.subway.line.Line;
import wooteco.subway.line.LineDao;
import wooteco.subway.line.section.Section;
import wooteco.subway.line.section.SectionDao;


@DisplayName("지하철역 Service 관련 기능")
class StationServiceTest extends UnitTest {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final Station gangnamStation = new Station(1L, "강남역");
    private final Station jamsilStation = new Station(2L, "잠실역");
    private final Station yeoksamStation = new Station(3L, "역삼역");

    public StationServiceTest(StationDao stationDao, LineDao lineDao,
        SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Test
    @DisplayName("지하철역을 저장한다")
    void save() {
        //given

        //when
        Station station = stationDao.save(new Station("강남역"));

        //then
        assertThat(station).usingRecursiveComparison().isEqualTo(gangnamStation);
    }

    @Test
    @DisplayName("모든 지하철역 가져온다")
    void findAll() {
        //given
        Station station1 = stationDao.save(gangnamStation);
        Station station2 = stationDao.save(jamsilStation);
        Station station3 = stationDao.save(yeoksamStation);

        List<Station> answerStations = Arrays.asList(station1, station2, station3);
        //when
        List<Station> stations = stationDao.findAll();

        //then
        assertThat(stations).hasSize(3)
            .usingRecursiveComparison()
            .isEqualTo(answerStations);
    }

    @Test
    @DisplayName("id를 이용하여 지하철 역을 삭제한다")
    void deleteById() {
        //given
        Station station1 = stationDao.save(gangnamStation);
        Station station2 = stationDao.save(jamsilStation);
        stationDao.save(yeoksamStation);

        List<Station> answerStations = Arrays.asList(station1, station2);

        //when
        stationDao.delete(3L);

        //then
        List<Station> stations = stationDao.findAll();
        assertThat(stations).hasSize(2)
            .usingRecursiveComparison()
            .isEqualTo(answerStations);
    }

    @Test
    @DisplayName("노선에 포함된 역을 가져온다")
    void findByLineId() {
        //given
        Station station1 = stationDao.save(gangnamStation);
        stationDao.save(jamsilStation);
        Station station2 = stationDao.save(yeoksamStation);
        lineDao.save(new Line("2호선", "green"));
        sectionDao.save(1L, new Section(1L, 1L, 3L, 10));

        List<Station> answerStations = Arrays.asList(station1, station2);
        //when
        List<Station> stations = stationDao.findByLineId(1L);

        //then
        assertThat(stations).hasSize(2)
            .usingRecursiveComparison()
            .isEqualTo(answerStations);
    }
}