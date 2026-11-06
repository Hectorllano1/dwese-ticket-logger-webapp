package org.alixar.daw2.hectorllano.dwese_ticket_logger_webapp.dao;

import org.alixar.daw2.hectorllano.dwese_ticket_logger_webapp.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository

public class RegionDAOImpl implements RegionDAO {
    /**
     * Lista todas las regiones de la base de datos.
     *
     * @return Lista de regiones
     * @throws SQLException
     */

    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);


    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate mediante el constructor
    public RegionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Region> listAllRegions() {
        String sql = "SELECT * FROM regions";
        return jdbcTemplate.query(sql, new
                BeanPropertyRowMapper<>(Region.class));
    }

    public void insertRegion(Region region) {
        String sql = "INSERT INTO regions (code, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, region.getCode(), region.getName());
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param region Región a actualizar
     * @throws SQLException
     */
    public void updateRegion(Region region) {
        String sql = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        jdbcTemplate.update(sql, region.getCode(), region.getName(),
                region.getId());
    }

    /**
     * Elimina una región de la base de datos.
     *
     * @param id ID de la región a eliminar
     * @throws SQLException
     */
    public void deleteRegion(Long id) {
        String sql = "DELETE FROM regions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de
     * datos,
     * ignorando mayúsculas.
     *
     * @param id el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    public Region getRegionById(Long id) {
        logger.info("Retrieving region by id: {}", id);
        String sql = "SELECT * FROM regions WHERE id = ?";
        try {
            Region region = jdbcTemplate.queryForObject(sql, new
                    BeanPropertyRowMapper<>(Region.class), id);
            logger.info("Region retrieved: {} - {}", region.getCode(),
                    region.getName());
            return region;
        } catch (Exception e) {
            logger.warn("No region found with id: {}", id);
            return null;
        }
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de
     * datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCode(String code) {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                code.toUpperCase());
        return count != null && count > 0;
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de
     * datos,
     * ignorando mayúsculas, pero excluyendo una región con un ID específico.
     *
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con
     * el ID dado),
     * false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}",
                code, id);
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                code.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id,
                exists);
        return exists;
    }
}

