package com.follysitou.movieflix_backend.auth.repositories;

import com.follysitou.movieflix_backend.auth.entities.ForgotPassword;
import com.follysitou.movieflix_backend.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {


    Optional<ForgotPassword> findByUser(User user);

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2 ")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
