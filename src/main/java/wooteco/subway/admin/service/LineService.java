package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public LineResponse save(Line line) {
		if (lineRepository.existsByName(line.getName())) {
			throw new RuntimeException();
		}
		return LineResponse.of(lineRepository.save(line));
	}

	public List<LineResponse> showLines() {
		return LineResponse.listOf(lineRepository.findAll());
	}

	public LineResponse updateLine(Long id, Line line) {
		if (lineRepository.existsByName(line.getName())) {
			throw new RuntimeException();
		}
		Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		persistLine.update(line);
		return LineResponse.of(lineRepository.save(persistLine));
	}

	public LineResponse showLine(Long id) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		return LineResponse.of(line);
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest lineStationCreateRequest) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		line.addLineStation(lineStationCreateRequest.toLineStation());
	}

	public void removeLineStation(Long lineId, Long stationId) {
		// TODO: 구현
	}

	public LineResponse findLineWithStationsById(Long id) {
		// TODO: 구현
		return new LineResponse();
	}
}
