package co.analisys.clases.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecuperacionService {

    private final JdbcTemplate jdbcTemplate;

    public RecuperacionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void guardarOffset(String topic, int partition, long offset) {
        jdbcTemplate.update("INSERT INTO kafka_offsets (topic, partition, last_offset) VALUES (?, ?, ?) " +
                        "ON CONFLICT (topic, partition) DO UPDATE SET last_offset = EXCLUDED.last_offset",
                topic, partition, offset);
    }

    public Map<TopicPartition, Long> cargarUltimoOffset() {
        Map<TopicPartition, Long> offsets = new HashMap<>();
        jdbcTemplate.query("SELECT topic, partition, last_offset FROM kafka_offsets",
                (rs, rowNum) -> offsets.put(new TopicPartition(rs.getString("topic"), rs.getInt("partition")),
                        rs.getLong("last_offset")));

        return offsets;
    }
}
