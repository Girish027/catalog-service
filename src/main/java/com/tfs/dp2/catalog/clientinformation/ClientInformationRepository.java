package com.tfs.dp2.catalog.clientinformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Repository
public interface ClientInformationRepository extends JpaRepository<ClientInformation, String> {

    ClientInformation findByInfoId(String infoId);

    @Query(value = "SELECT info_id FROM catalog.client_information WHERE info_name = ?1 AND info_type = ?2",nativeQuery = true)
    String findClientIdByClientNameAndAccountType(String clientName, String accountType);

    @Query(value = "SELECT info_id FROM catalog.client_information WHERE info_name = ?1 AND info_type = ?2 And parent_info_id =?3",nativeQuery = true)
    String findClientIdByClientNameAndAccountTypeAndParentAccount(String clientName, String accountType, String parentId);

    @Query(value = "select * from client_information where info_id = (select parent_info_id from client_information where info_name=:accountName and info_type='Account')",nativeQuery = true)
    Optional<ClientInformation> findClientInfoByAccountName(@Param("accountName") String accountName);

    @Query(value = "SELECT info_id FROM catalog.client_information WHERE parent_info_id = ?1 AND info_type = ?2 ORDER BY info_id LIMIT 1",nativeQuery = true)
    String findClientHDFSFolderIdByParentInfoIdAndAccountType(String parentInfoId, String accountType);

    @Modifying
    @Query(value = "DELETE FROM catalog.client_information WHERE info_id IN ?1", nativeQuery = true)
    void deleteByClientIds(List<String> clientIdList);

    public enum ClientInfoType {
        HDFS_FOLDER("HDFSFolder"),
        CLIENT("Client"),
        ACCOUNT("Account");

        private String key;

        ClientInfoType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
