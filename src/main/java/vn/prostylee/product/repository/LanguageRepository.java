package vn.prostylee.product.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Language;

@Repository
public interface LanguageRepository extends BaseRepository<Language, Long> {
}
