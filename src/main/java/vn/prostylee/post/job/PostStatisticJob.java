package vn.prostylee.post.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.post.entity.PostStatistic;
import vn.prostylee.post.repository.PostStatisticRepository;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.response.LikeCountResponse;
import vn.prostylee.useractivity.service.UserLikeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@DisallowConcurrentExecution
// This annotation tells Quartz that a given Job definition (that is, a JobDetail instance) does not run concurrently.
public class PostStatisticJob extends QuartzJobBean {

    public static final int LIMIT = 50;

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private PostStatisticRepository postStatisticRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.debug("PostStatisticJob executing ...");
        countNumberOfLike();
        countNumberOfComment();
    }


    private void countNumberOfLike() {
        int page = 0;
        Page<LikeCountResponse> likeCountResponsePage = userLikeService.countNumberLike(new PagingParam(page, LIMIT), TargetType.POST.toString());
        log.debug("totalPages={}, totalElements={}, postLikeSize={}", likeCountResponsePage.getTotalPages(), likeCountResponsePage.getTotalElements(), likeCountResponsePage.getNumberOfElements());
        while (likeCountResponsePage.getNumberOfElements() > 0) {
            upsertLikeStatistic(likeCountResponsePage.getContent());
            page++;
            likeCountResponsePage = userLikeService.countNumberLike(new PagingParam(page, LIMIT), TargetType.POST.toString());
            log.debug("totalPages={}, totalElements={}, postLikeSize={}", likeCountResponsePage.getTotalPages(), likeCountResponsePage.getTotalElements(), likeCountResponsePage.getNumberOfElements());
        }
    }

    private void countNumberOfComment() {
        // TODO
    }

    private void upsertLikeStatistic(List<LikeCountResponse> likeCountResponses) {
        final Map<Long, Long> mapPostCount = likeCountResponses.stream()
                .collect(Collectors.toMap(LikeCountResponse::getId, LikeCountResponse::getCount));

        List<PostStatistic> statistics = postStatisticRepository.findByPostIds(mapPostCount.keySet());

        statistics.forEach(postStatistic -> {
            postStatistic.setNumberOfLike(mapPostCount.getOrDefault(postStatistic.getId(), 0L));
            mapPostCount.remove(postStatistic.getId());
        });

        List<PostStatistic> adds = mapPostCount.keySet()
                .stream()
                .map(postId -> {
                    return PostStatistic.builder()
                            .id(postId)
                            .numberOfLike(mapPostCount.getOrDefault(postId, 0L))
                            .build();
                })
                .collect(Collectors.toList());

        statistics.addAll(adds);

        postStatisticRepository.saveAll(statistics);
    }
}
