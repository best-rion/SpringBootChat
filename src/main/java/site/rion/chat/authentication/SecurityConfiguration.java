package site.rion.chat.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	        //declares which Page(URL) will have What access type
	        http
	        	.csrf((csrf)->
	        		csrf.disable()
	        	)
	        	.authorizeHttpRequests((authReq) ->
		        	authReq
		        		.requestMatchers("/", "/receiver", "/app/**", "/topic/**", "/gs-guide-websocket").hasAuthority("USER")
		        		.requestMatchers("/signup", "/css/**", "/js/**", "/images/**").permitAll()
		        		.anyRequest().authenticated()
	        	)
	        	.formLogin((loginCustomizer) ->
	        		loginCustomizer
		        		.loginPage("/login")
		        		.defaultSuccessUrl("/", true)
		        		.permitAll()
	        	)
	        	.logout((logout) -> 
		        	logout
		        		.logoutSuccessUrl("/login")
		        		.permitAll()
		        );
	        					
	        
	    return http.build();
	    }
	    
	    
	    
	    @Bean
		public AuthenticationManager authenticationManager(
				UserDetailsService userDetailsService,
				PasswordEncoder passwordEncoder) {
			DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
			authenticationProvider.setUserDetailsService(userDetailsService);
			authenticationProvider.setPasswordEncoder(passwordEncoder);

			return new ProviderManager(authenticationProvider);
		}
	    
		
		@Bean
		public PasswordEncoder getPasswordEncoder()
		{
			return new BCryptPasswordEncoder();
		}
}