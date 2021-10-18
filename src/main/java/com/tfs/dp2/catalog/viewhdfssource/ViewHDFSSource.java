package com.tfs.dp2.catalog.viewhdfssource;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Data
@Entity
@Table(name = "view_hdfs_source")
@EntityListeners(AuditingEntityListener.class)
public class ViewHDFSSource implements Serializable{

	@Id
	@Column(name = "hdfs_source_id")
	private String hdfsSourceId;

	@NotNull
	@NotEmpty
    @Column(name = "hdfs_source_view_id")
    private String hdfsSourceName;

	@Column(name = "view_id")
	private String viewId;

	@NotNull
	@NotEmpty
	@Column(name = "hdfs_path")
	private String hdfsPath;

	@Column(columnDefinition = "bigint", name = "created_unixtimestamps")
	private long creatdUnixtimestamps = System.currentTimeMillis();

	@Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
	private long modifiedUnixtimestamps = System.currentTimeMillis();

	@Column(name = "created_by")
	private String createdBy = "System";

	@Column(name = "modified_by")
	private String modifiedBy = "System";
}
