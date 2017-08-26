package com.smusic.app.repository;

import com.smusic.app.repository.dto.SongData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sergey on 25.08.17.
 */
@Transactional
public interface SongDataDao extends PagingAndSortingRepository<SongData, Long> {
}