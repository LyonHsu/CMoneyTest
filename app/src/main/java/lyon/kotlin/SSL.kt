package lyon.kotlin


import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager


import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier
import javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory

object SSL {
    val TAG ="SSL"

    /**
     * 忽略驗證https
     */
    @Throws(Exception::class)
    fun igoreVerify() {
        ignoreVerifyHttpsHostName()
    }
    /**
     * 忽略驗證https
     */
    fun ignoreVerifyHttpsHostName() {
        val hv = object : HostnameVerifier {
            override fun verify(urlHostName: String, session: SSLSession): Boolean {

                LogL.d(TAG,"Warning: URL Host: " + urlHostName + " vs. " + session.peerHost)
                return true
            }

        }

        HttpsURLConnection.setDefaultHostnameVerifier(hv)
    }

    /**
     * 忽略驗證https
     */
    @Throws(Exception::class)
    fun ignoreVerifyHttpsTrustManager() :SSLContext{
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(
                certs: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun checkServerTrusted(
                certs: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }
        })

        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        return sc
    }


}