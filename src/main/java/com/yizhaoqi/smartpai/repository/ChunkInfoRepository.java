package com.yizhaoqi.smartpai.repository;

import com.yizhaoqi.smartpai.model.ChunkInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChunkInfoRepository extends JpaRepository<ChunkInfo, Long> {

    // Optional<ChunkInfo> findById(Long id);

    List<ChunkInfo> findByFileMd5OrderByChunkIndexAsc(String fileMd5);

    // List<ChunkInfo> findByFileMd5AndChunkIndex(String fileMd5, int chunkIndex);

}
