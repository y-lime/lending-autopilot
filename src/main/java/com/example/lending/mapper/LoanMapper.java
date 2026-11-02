package com.example.lending.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.lending.domain.Loan;

@Mapper
public interface LoanMapper {

    @Insert("""
            INSERT INTO loan (equipment_id, borrower, lent_on, due_on)
            VALUES (#{equipmentId}, #{borrower}, #{lentOn}, #{dueOn})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Loan loan);

    @Select("""
            SELECT id, equipment_id, borrower, lent_on, due_on, returned_on
              FROM loan WHERE id = #{id}
            """)
    Optional<Loan> findById(@Param("id") long id);

    @Select("SELECT COUNT(*) FROM loan WHERE borrower = #{borrower} AND returned_on IS NULL")
    int countActiveByBorrower(@Param("borrower") String borrower);

    @Update("UPDATE loan SET returned_on = #{returnedOn} WHERE id = #{id} AND returned_on IS NULL")
    int markReturned(@Param("id") long id, @Param("returnedOn") LocalDate returnedOn);
}
