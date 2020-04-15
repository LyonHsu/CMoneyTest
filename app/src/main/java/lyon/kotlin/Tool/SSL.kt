package lyon.kotlin.Tool


import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.*


object SSL {
    val TAG ="SSL"

    /**
     * 忽略驗證https
     */
    @Throws(Exception::class)
    fun igoreVerify() {
        ignoreVerifyHttpsHostName()
        ignoreVerifyHttpsTrustManager()
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
    fun ignoreVerifyHttpsTrustManager(vararg certificates: InputStream) :SSLSocketFactory{
        val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
        var keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        var index = 0
        for ( certificate in certificates){
            val certificateAlias = Integer.toString(index++)
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
            try{
                if (certificate != null)
                    certificate.close();
            } catch ( e: IOException){
                e.printStackTrace() ;
            }
        }

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
        //取得SSL的SSLContext实例
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        return sc.socketFactory
    }


}