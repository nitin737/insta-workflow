package com.instaworkflow.api.service;

import com.instaworkflow.api.entity.Job;
import com.instaworkflow.api.entity.JobStatus;
import com.instaworkflow.api.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherScheduler {

    private static final Logger log = LoggerFactory.getLogger(PublisherScheduler.class);
    private final JobRepository jobRepository;
    private final InstagramPublisherService publisherService;

    public PublisherScheduler(JobRepository jobRepository, InstagramPublisherService publisherService) {
        this.jobRepository = jobRepository;
        this.publisherService = publisherService;
    }

    @Scheduled(fixedDelay = 60000) // Run every 60 seconds
    public void schedulePublishing() {
        log.debug("Checking for pending jobs to publish...");
        List<Job> pendingJobs = jobRepository.findByStatus(JobStatus.COMPLETED);

        if (pendingJobs.isEmpty()) {
            return;
        }

        log.info("Found {} jobs ready for publishing", pendingJobs.size());

        for (Job job : pendingJobs) {
            try {
                updateJobStatus(job, JobStatus.PUBLISHING);
                publisherService.publishCarousel(job);
                updateJobStatus(job, JobStatus.PUBLISHED);
            } catch (Exception e) {
                log.error("Failed to publish job {}: {}", job.getId(), e.getMessage(), e);
                updateJobStatus(job, JobStatus.PUBLISH_FAILED);
            }
        }
    }

    private void updateJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRepository.save(job);
        log.debug("Updated job {} status to {}", job.getId(), status);
    }
}
