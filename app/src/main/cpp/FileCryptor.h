#import <algorithm>
#import <string>
#import <fstream>
#import <iostream>
#import "aes/aes.h"
#include <sstream>

#ifndef NDKEXPERIMENTS_FILEINSTANCE_H
#define NDKEXPERIMENTS_FILEINSTANCE_H

/*
 * This class helps us to crypt files
 */
class FileCryptor {

private:

    const int AES_KEY_SIZE=256;
    const std::string ANNEX="crypted_";

    int size_;

    std::string name_;
    std::string path_;

    unsigned char* code_;

    /**
     * Function return name and type of file
     */
    std::string getFileNameFromPath(const std::string& filePath);
    /**
     * This function makes hash from user's special code
     */
    unsigned char* hashCode(std::string &userCode);

    std::string getCryptedFileName();
    /**
     * Getting original file path
     */
    const char* originalFilePath();
    /**
   * Getting original path for crypted file
   */
    const char* encryptedFilePath();

    template <typename T>
    std::string to_string(T value) {
        std::ostringstream os ;
        os << value ;
        return os.str() ;
    }

    FileCryptor(){}

    void zeroingBuffer(unsigned char * buffer);
    bool bufferIsEmpty(unsigned char* buffer);

public:

    static FileCryptor& Instance() {
        static FileCryptor instance;
        return instance;
    }

    int getSize() const;
    std::string getName() const;

    bool encryptFile(std::string& path,std::string& code);


};


#endif //NDKEXPERIMENTS_FILEINSTANCE_H
