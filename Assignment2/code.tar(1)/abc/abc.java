startingcode2/                                                                                      0000755 0001751 0001751 00000000000 13157110414 011423  5                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     startingcode2/AntTweakBar.h                                                                         0000600 0001751 0001751 00000036434 13157110116 013740  0                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     // ----------------------------------------------------------------------------
//
//  @file       AntTweakBar.h
//
//  @brief      AntTweakBar is a light and intuitive graphical user interface 
//              that can be readily integrated into OpenGL and DirectX 
//              applications in order to interactively tweak parameters.
//
//  @author     Philippe Decaudin
//
//  @doc        http://anttweakbar.sourceforge.net/doc
//
//  @license    This file is part of the AntTweakBar library.
//              AntTweakBar is a free software released under the zlib license.
//              For conditions of distribution and use, see License.txt
//
// ----------------------------------------------------------------------------


#if !defined TW_INCLUDED
#define TW_INCLUDED

#include <stddef.h>

#define TW_VERSION  116 // Version Mmm : M=Major mm=minor (e.g., 102 is version 1.02)


#ifdef  __cplusplus
#   if defined(_MSC_VER)
#       pragma warning(push)
#       pragma warning(disable: 4995 4530)
#       include <string>
#       pragma warning(pop)
#   else
#       include <string>
#   endif
    extern "C" {
#endif  // __cplusplus


// ----------------------------------------------------------------------------
//  OS specific definitions
// ----------------------------------------------------------------------------

#if (defined(_WIN32) || defined(_WIN64)) && !defined(TW_STATIC)
#   define TW_CALL          __stdcall
#   define TW_CDECL_CALL    __cdecl
#   define TW_EXPORT_API    __declspec(dllexport)
#   define TW_IMPORT_API    __declspec(dllimport)
#else
#   define TW_CALL
#   define TW_CDECL_CALL
#   define TW_EXPORT_API
#   define TW_IMPORT_API
#endif

#if defined TW_EXPORTS
#   define TW_API TW_EXPORT_API
#elif defined TW_STATIC
#   define TW_API
#   if defined(_MSC_VER) && !defined(TW_NO_LIB_PRAGMA)
#       ifdef _WIN64
#           pragma comment(lib, "AntTweakBarStatic64")
#       else
#           pragma comment(lib, "AntTweakBarStatic")
#       endif
#   endif
#else
#   define TW_API TW_IMPORT_API
#   if defined(_MSC_VER) && !defined(TW_NO_LIB_PRAGMA)
#       ifdef _WIN64
#           pragma comment(lib, "AntTweakBar64")
#       else
#           pragma comment(lib, "AntTweakBar")
#       endif
#   endif
#endif


// ----------------------------------------------------------------------------
//  Bar functions and definitions
// ----------------------------------------------------------------------------

typedef struct CTwBar TwBar; // structure CTwBar is not exposed.

TW_API TwBar *      TW_CALL TwNewBar(const char *barName);
TW_API int          TW_CALL TwDeleteBar(TwBar *bar);
TW_API int          TW_CALL TwDeleteAllBars();
TW_API int          TW_CALL TwSetTopBar(const TwBar *bar);
TW_API TwBar *      TW_CALL TwGetTopBar();
TW_API int          TW_CALL TwSetBottomBar(const TwBar *bar);
TW_API TwBar *      TW_CALL TwGetBottomBar();
TW_API const char * TW_CALL TwGetBarName(const TwBar *bar);
TW_API int          TW_CALL TwGetBarCount();
TW_API TwBar *      TW_CALL TwGetBarByIndex(int barIndex);
TW_API TwBar *      TW_CALL TwGetBarByName(const char *barName);
TW_API int          TW_CALL TwRefreshBar(TwBar *bar);

// ----------------------------------------------------------------------------
//  Var functions and definitions
// ----------------------------------------------------------------------------

typedef enum ETwType
{
    TW_TYPE_UNDEF   = 0,
#ifdef __cplusplus
    TW_TYPE_BOOLCPP = 1,
#endif // __cplusplus
    TW_TYPE_BOOL8   = 2,
    TW_TYPE_BOOL16,
    TW_TYPE_BOOL32,
    TW_TYPE_CHAR,
    TW_TYPE_INT8,
    TW_TYPE_UINT8,
    TW_TYPE_INT16,
    TW_TYPE_UINT16,
    TW_TYPE_INT32,
    TW_TYPE_UINT32,
    TW_TYPE_FLOAT,
    TW_TYPE_DOUBLE,
    TW_TYPE_COLOR32,    // 32 bits color. Order is RGBA if API is OpenGL or Direct3D10, and inversed if API is Direct3D9 (can be modified by defining 'colorOrder=...', see doc)
    TW_TYPE_COLOR3F,    // 3 floats color. Order is RGB.
    TW_TYPE_COLOR4F,    // 4 floats color. Order is RGBA.
    TW_TYPE_CDSTRING,   // Null-terminated C Dynamic String (pointer to an array of char dynamically allocated with malloc/realloc/strdup)
#ifdef __cplusplus
# if defined(_MSC_VER) && (_MSC_VER == 1600)
    TW_TYPE_STDSTRING = (0x2ffe0000+sizeof(std::string)),  // VS2010 C++ STL string (std::string)
# else
    TW_TYPE_STDSTRING = (0x2fff0000+sizeof(std::string)),  // C++ STL string (std::string)
# endif
#endif // __cplusplus
    TW_TYPE_QUAT4F = TW_TYPE_CDSTRING+2, // 4 floats encoding a quaternion {qx,qy,qz,qs}
    TW_TYPE_QUAT4D,     // 4 doubles encoding a quaternion {qx,qy,qz,qs}
    TW_TYPE_DIR3F,      // direction vector represented by 3 floats
    TW_TYPE_DIR3D       // direction vector represented by 3 doubles
} TwType;
#define TW_TYPE_CSSTRING(n) ((TwType)(0x30000000+((n)&0xfffffff))) // Null-terminated C Static String of size n (defined as char[n], with n<2^28)

typedef void (TW_CALL * TwSetVarCallback)(const void *value, void *clientData);
typedef void (TW_CALL * TwGetVarCallback)(void *value, void *clientData);
typedef void (TW_CALL * TwButtonCallback)(void *clientData);

TW_API int      TW_CALL TwAddVarRW(TwBar *bar, const char *name, TwType type, void *var, const char *def);
TW_API int      TW_CALL TwAddVarRO(TwBar *bar, const char *name, TwType type, const void *var, const char *def);
TW_API int      TW_CALL TwAddVarCB(TwBar *bar, const char *name, TwType type, TwSetVarCallback setCallback, TwGetVarCallback getCallback, void *clientData, const char *def);
TW_API int      TW_CALL TwAddButton(TwBar *bar, const char *name, TwButtonCallback callback, void *clientData, const char *def);
TW_API int      TW_CALL TwAddSeparator(TwBar *bar, const char *name, const char *def);
TW_API int      TW_CALL TwRemoveVar(TwBar *bar, const char *name);
TW_API int      TW_CALL TwRemoveAllVars(TwBar *bar);

typedef struct CTwEnumVal
{
    int           Value;
    const char *  Label;
} TwEnumVal;
typedef struct CTwStructMember
{
    const char *  Name;
    TwType        Type;
    size_t        Offset;
    const char *  DefString;
} TwStructMember;
typedef void (TW_CALL * TwSummaryCallback)(char *summaryString, size_t summaryMaxLength, const void *value, void *clientData);

TW_API int      TW_CALL TwDefine(const char *def);
TW_API TwType   TW_CALL TwDefineEnum(const char *name, const TwEnumVal *enumValues, unsigned int nbValues);
TW_API TwType   TW_CALL TwDefineEnumFromString(const char *name, const char *enumString);
TW_API TwType   TW_CALL TwDefineStruct(const char *name, const TwStructMember *structMembers, unsigned int nbMembers, size_t structSize, TwSummaryCallback summaryCallback, void *summaryClientData);

typedef void (TW_CALL * TwCopyCDStringToClient)(char **destinationClientStringPtr, const char *sourceString);
TW_API void     TW_CALL TwCopyCDStringToClientFunc(TwCopyCDStringToClient copyCDStringFunc);
TW_API void     TW_CALL TwCopyCDStringToLibrary(char **destinationLibraryStringPtr, const char *sourceClientString);
#ifdef __cplusplus
typedef void (TW_CALL * TwCopyStdStringToClient)(std::string& destinationClientString, const std::string& sourceString);
TW_API void     TW_CALL TwCopyStdStringToClientFunc(TwCopyStdStringToClient copyStdStringToClientFunc);
TW_API void     TW_CALL TwCopyStdStringToLibrary(std::string& destinationLibraryString, const std::string& sourceClientString);
#endif // __cplusplus

typedef enum ETwParamValueType
{
    TW_PARAM_INT32,
    TW_PARAM_FLOAT,
    TW_PARAM_DOUBLE,
    TW_PARAM_CSTRING // Null-terminated array of char (ie, c-string)
} TwParamValueType;
TW_API int      TW_CALL TwGetParam(TwBar *bar, const char *varName, const char *paramName, TwParamValueType paramValueType, unsigned int outValueMaxCount, void *outValues);
TW_API int      TW_CALL TwSetParam(TwBar *bar, const char *varName, const char *paramName, TwParamValueType paramValueType, unsigned int inValueCount, const void *inValues);


// ----------------------------------------------------------------------------
//  Management functions and definitions
// ----------------------------------------------------------------------------

typedef enum ETwGraphAPI
{
    TW_OPENGL           = 1,
    TW_DIRECT3D9        = 2,
    TW_DIRECT3D10       = 3,
    TW_DIRECT3D11       = 4,
    TW_OPENGL_CORE      = 5
} TwGraphAPI;

TW_API int      TW_CALL TwInit(TwGraphAPI graphAPI, void *device);
TW_API int      TW_CALL TwTerminate();

TW_API int      TW_CALL TwDraw();
TW_API int      TW_CALL TwWindowSize(int width, int height);

TW_API int      TW_CALL TwSetCurrentWindow(int windowID); // multi-windows support
TW_API int      TW_CALL TwGetCurrentWindow();
TW_API int      TW_CALL TwWindowExists(int windowID);

typedef enum ETwKeyModifier
{
    TW_KMOD_NONE        = 0x0000,   // same codes as SDL keysym.mod
    TW_KMOD_SHIFT       = 0x0003,
    TW_KMOD_CTRL        = 0x00c0,
    TW_KMOD_ALT         = 0x0100,
    TW_KMOD_META        = 0x0c00
} TwKeyModifier;
typedef enum EKeySpecial
{
    TW_KEY_BACKSPACE    = '\b',
    TW_KEY_TAB          = '\t',
    TW_KEY_CLEAR        = 0x0c,
    TW_KEY_RETURN       = '\r',
    TW_KEY_PAUSE        = 0x13,
    TW_KEY_ESCAPE       = 0x1b,
    TW_KEY_SPACE        = ' ',
    TW_KEY_DELETE       = 0x7f,
    TW_KEY_UP           = 273,      // same codes and order as SDL 1.2 keysym.sym
    TW_KEY_DOWN,
    TW_KEY_RIGHT,
    TW_KEY_LEFT,
    TW_KEY_INSERT,
    TW_KEY_HOME,
    TW_KEY_END,
    TW_KEY_PAGE_UP,
    TW_KEY_PAGE_DOWN,
    TW_KEY_F1,
    TW_KEY_F2,
    TW_KEY_F3,
    TW_KEY_F4,
    TW_KEY_F5,
    TW_KEY_F6,
    TW_KEY_F7,
    TW_KEY_F8,
    TW_KEY_F9,
    TW_KEY_F10,
    TW_KEY_F11,
    TW_KEY_F12,
    TW_KEY_F13,
    TW_KEY_F14,
    TW_KEY_F15,
    TW_KEY_LAST
} TwKeySpecial;

TW_API int      TW_CALL TwKeyPressed(int key, int modifiers);
TW_API int      TW_CALL TwKeyTest(int key, int modifiers);

typedef enum ETwMouseAction
{
    TW_MOUSE_RELEASED,
    TW_MOUSE_PRESSED  
} TwMouseAction;
typedef enum ETwMouseButtonID
{
    TW_MOUSE_LEFT       = 1,    // same code as SDL_BUTTON_LEFT
    TW_MOUSE_MIDDLE     = 2,    // same code as SDL_BUTTON_MIDDLE
    TW_MOUSE_RIGHT      = 3     // same code as SDL_BUTTON_RIGHT
} TwMouseButtonID;

TW_API int      TW_CALL TwMouseButton(TwMouseAction action, TwMouseButtonID button);
TW_API int      TW_CALL TwMouseMotion(int mouseX, int mouseY);
TW_API int      TW_CALL TwMouseWheel(int pos);

TW_API const char * TW_CALL TwGetLastError();
typedef void (TW_CALL * TwErrorHandler)(const char *errorMessage);
TW_API void     TW_CALL TwHandleErrors(TwErrorHandler errorHandler);


// ----------------------------------------------------------------------------
//  Helper functions to translate events from some common window management
//  frameworks to AntTweakBar.
//  They call TwKeyPressed, TwMouse* and TwWindowSize for you (implemented in
//  files TwEventWin.c TwEventSDL*.c TwEventGLFW.c TwEventGLUT.c)
// ----------------------------------------------------------------------------

// For Windows message proc
#ifndef _W64    // Microsoft specific (detection of 64 bits portability issues)
#   define _W64
#endif  // _W64
#ifdef _WIN64
    TW_API int  TW_CALL TwEventWin(void *wnd, unsigned int msg, unsigned __int64 _W64 wParam, __int64 _W64 lParam);
#else
    TW_API int  TW_CALL TwEventWin(void *wnd, unsigned int msg, unsigned int _W64 wParam, int _W64 lParam);
#endif
#define TwEventWin32    TwEventWin // For compatibility with AntTweakBar versions prior to 1.11

// For libSDL event loop
TW_API int      TW_CALL TwEventSDL(const void *sdlEvent, unsigned char sdlMajorVersion, unsigned char sdlMinorVersion);

// For GLFW event callbacks
// You should define GLFW_CDECL before including AntTweakBar.h if your version of GLFW uses cdecl calling convensions
#ifdef GLFW_CDECL
    TW_API int TW_CDECL_CALL TwEventMouseButtonGLFWcdecl(int glfwButton, int glfwAction);
    TW_API int TW_CDECL_CALL TwEventKeyGLFWcdecl(int glfwKey, int glfwAction);
    TW_API int TW_CDECL_CALL TwEventCharGLFWcdecl(int glfwChar, int glfwAction);
    TW_API int TW_CDECL_CALL TwEventMousePosGLFWcdecl(int mouseX, int mouseY);
    TW_API int TW_CDECL_CALL TwEventMouseWheelGLFWcdecl(int wheelPos);
#   define TwEventMouseButtonGLFW TwEventMouseButtonGLFWcdecl
#   define TwEventKeyGLFW         TwEventKeyGLFWcdecl
#   define TwEventCharGLFW        TwEventCharGLFWcdecl
#   define TwEventMousePosGLFW    TwEventMousePosGLFWcdecl
#   define TwEventMouseWheelGLFW  TwEventMouseWheelGLFWcdecl
#else
    TW_API int  TW_CALL TwEventMouseButtonGLFW(int glfwButton, int glfwAction);
    TW_API int  TW_CALL TwEventKeyGLFW(int glfwKey, int glfwAction);
    TW_API int  TW_CALL TwEventCharGLFW(int glfwChar, int glfwAction);
#   define TwEventMousePosGLFW     TwMouseMotion
#   define TwEventMouseWheelGLFW   TwMouseWheel
#endif

// For GLUT event callbacks (Windows calling convention for GLUT callbacks is cdecl)
#if defined(_WIN32) || defined(_WIN64)
#   define TW_GLUT_CALL TW_CDECL_CALL
#else
#   define TW_GLUT_CALL
#endif
TW_API int TW_GLUT_CALL TwEventMouseButtonGLUT(int glutButton, int glutState, int mouseX, int mouseY);
TW_API int TW_GLUT_CALL TwEventMouseMotionGLUT(int mouseX, int mouseY);
TW_API int TW_GLUT_CALL TwEventKeyboardGLUT(unsigned char glutKey, int mouseX, int mouseY);
TW_API int TW_GLUT_CALL TwEventSpecialGLUT(int glutKey, int mouseX, int mouseY);
TW_API int TW_CALL      TwGLUTModifiersFunc(int (TW_CALL *glutGetModifiersFunc)(void));
typedef void (TW_GLUT_CALL *GLUTmousebuttonfun)(int glutButton, int glutState, int mouseX, int mouseY);
typedef void (TW_GLUT_CALL *GLUTmousemotionfun)(int mouseX, int mouseY);
typedef void (TW_GLUT_CALL *GLUTkeyboardfun)(unsigned char glutKey, int mouseX, int mouseY);
typedef void (TW_GLUT_CALL *GLUTspecialfun)(int glutKey, int mouseX, int mouseY);

// For SFML event loop
TW_API int      TW_CALL TwEventSFML(const void *sfmlEvent, unsigned char sfmlMajorVersion, unsigned char sfmlMinorVersion);

// For X11 event loop
#if defined(_UNIX)
    TW_API int TW_CDECL_CALL TwEventX11(void *xevent);
#endif

// ----------------------------------------------------------------------------
//  Make sure the types have the right sizes
// ----------------------------------------------------------------------------

#define TW_COMPILE_TIME_ASSERT(name, x) typedef int TW_DUMMY_ ## name[(x) * 2 - 1]

TW_COMPILE_TIME_ASSERT(TW_CHAR,    sizeof(char)    == 1);
TW_COMPILE_TIME_ASSERT(TW_SHORT,   sizeof(short)   == 2);
TW_COMPILE_TIME_ASSERT(TW_INT,     sizeof(int)     == 4);
TW_COMPILE_TIME_ASSERT(TW_FLOAT,   sizeof(float)   == 4);
TW_COMPILE_TIME_ASSERT(TW_DOUBLE,  sizeof(double)  == 8);

// Check pointer size on Windows
#if !defined(_WIN64) && defined(_WIN32)
    // If the following assert failed, the platform is not 32-bit and _WIN64 is not defined.
    // When targetting 64-bit Windows platform, _WIN64 must be defined.
    TW_COMPILE_TIME_ASSERT(TW_PTR32, sizeof(void*) == 4);
#elif defined(_WIN64)
    // If the following assert failed, _WIN64 is defined but the targeted platform is not 64-bit.
    TW_COMPILE_TIME_ASSERT(TW_PTR64, sizeof(void*) == 8);
#endif

//  ---------------------------------------------------------------------------


#ifdef  __cplusplus
    }   // extern "C"
#endif  // __cplusplus


#endif  // !defined TW_INCLUDED
                                                                                                                                                                                                                                    startingcode2/vmath.h                                                                               0000600 0001751 0001751 00000042556 13157110117 012717  0                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     #ifndef __VMATH_H__
#define __VMATH_H__


#define _USE_MATH_DEFINES  1 // Include constants defined in math.h
#include <math.h>

namespace vmath
{

template <typename T> 
inline T radians(T angleInRadians)
{
	return angleInRadians * static_cast<T>(180.0/M_PI);
}

template <const bool cond>
class ensure
{
public:
    inline ensure() { switch (false) { case false: case cond: break; } }
};

template <typename T, const int len> class vecN;

template <typename T, const int len>
class vecN
{
public:
    typedef class vecN<T,len> my_type;

    // Default constructor does nothing, just like built-in types
    inline vecN()
    {
        // Uninitialized variable
    }

    // Copy constructor
    inline vecN(const vecN& that)
    {
        assign(that);
    }

    // Construction from scalar
    inline vecN(T s)
    {
        int n;
        for (n = 0; n < len; n++)
        {
            data[n] = s;
        }
    }

    // Assignment operator
    inline vecN& operator=(const vecN& that)
    {
        assign(that);
        return *this;
    }

    inline vecN operator+(const vecN& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] + that.data[n];
        return result;
    }

    inline vecN& operator+=(const vecN& that)
    {
        return (*this = *this + that);
    }

    inline vecN operator-() const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = -data[n];
        return result;
    }

    inline vecN operator-(const vecN& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] - that.data[n];
        return result;
    }

    inline vecN& operator-=(const vecN& that)
    {
        return (*this = *this - that);
    }

    inline vecN operator*(const vecN& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] * that.data[n];
        return result;
    }

    inline vecN& operator*=(const vecN& that)
    {
        return (*this = *this * that);
    }

    inline vecN operator*(const T& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] * that;
        return result;
    }

    inline vecN& operator*=(const T& that)
    {
        assign(*this * that);

        return *this;
    }

    inline vecN operator/(const vecN& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] * that.data[n];
        return result;
    }

    inline vecN& operator/=(const vecN& that)
    {
        assign(*this * that);

        return *this;
    }

    inline vecN operator/(const T& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < len; n++)
            result.data[n] = data[n] / that;
        return result;
    }

    inline vecN& operator/(const T& that)
    {
        assign(*this / that);
    }

    inline T& operator[](int n) { return data[n]; }
    inline const T& operator[](int n) const { return data[n]; }

    inline static int size(void) { return len; }

    inline operator const T* () const { return &data[0]; }

protected:
    T data[len];

    inline void assign(const vecN& that)
    {
        int n;
        for (n = 0; n < len; n++)
            data[n] = that.data[n];
    }
};

template <typename T>
class Tvec2 : public vecN<T,2>
{
public:
    typedef vecN<T,2> base;

    // Uninitialized variable
    inline Tvec2() {}
    // Copy constructor
    inline Tvec2(const base& v) : base(v) {}

    // vec2(x, y);
    inline Tvec2(T x, T y)
    {
        base::data[0] = x;
        base::data[1] = y;
    }
};

template <typename T>
class Tvec3 : public vecN<T,3>
{
public:
    typedef vecN<T,3> base;

    // Uninitialized variable
    inline Tvec3() {}

    // Copy constructor
    inline Tvec3(const base& v) : base(v) {}

    // vec3(x, y, z);
    inline Tvec3(T x, T y, T z)
    {
        base::data[0] = x;
        base::data[1] = y;
        base::data[2] = z;
    }

    // vec3(v, z);
    inline Tvec3(const Tvec2<T>& v, T z)
    {
        base::data[0] = v[0];
        base::data[1] = v[1];
        base::data[2] = z;
    }

    // vec3(x, v)
    inline Tvec3(T x, const Tvec2<T>& v)
    {
        base::data[0] = x;
        base::data[1] = v[0];
        base::data[2] = v[1];
    }
};

template <typename T>
class Tvec4 : public vecN<T,4>
{
public:
    typedef vecN<T,4> base;

    // Uninitialized variable
    inline Tvec4() {}

    // Copy constructor
    inline Tvec4(const base& v) : base(v) {}

    // vec4(x, y, z, w);
    inline Tvec4(T x, T y, T z, T w)
    {
        base::data[0] = x;
        base::data[1] = y;
        base::data[2] = z;
        base::data[3] = w;
    }

    // vec4(v, z, w);
    inline Tvec4(const Tvec2<T>& v, T z, T w)
    {
        base::data[0] = v[0];
        base::data[1] = v[1];
        base::data[2] = z;
        base::data[3] = w;
    }

    // vec4(x, v, w);
    inline Tvec4(T x, const Tvec2<T>& v, T w)
    {
        base::data[0] = x;
        base::data[1] = v[0];
        base::data[2] = v[1];
        base::data[3] = w;
    }

    // vec4(x, y, v);
    inline Tvec4(T x, T y, const Tvec2<T>& v)
    {
        base::data[0] = x;
        base::data[1] = y;
        base::data[2] = v[0];
        base::data[3] = v[1];
    }

    // vec4(v1, v2);
    inline Tvec4(const Tvec2<T>& u, const Tvec2<T>& v)
    {
        base::data[0] = u[0];
        base::data[1] = u[1];
        base::data[2] = v[0];
        base::data[3] = v[1];
    }

    // vec4(v, w);
    inline Tvec4(const Tvec3<T>& v, T w)
    {
        base::data[0] = v[0];
        base::data[1] = v[1];
        base::data[2] = v[2];
        base::data[3] = w;
    }

    // vec4(x, v);
    inline Tvec4(T x, const Tvec3<T>& v)
    {
        base::data[0] = x;
        base::data[1] = v[0];
        base::data[2] = v[1];
        base::data[3] = v[2];
    }
};

typedef Tvec2<float> vec2;
typedef Tvec2<int> ivec2;
typedef Tvec2<unsigned int> uvec2;
typedef Tvec2<double> dvec2;

typedef Tvec3<float> vec3;
typedef Tvec3<int> ivec3;
typedef Tvec3<unsigned int> uvec3;
typedef Tvec3<double> dvec3;

typedef Tvec4<float> vec4;
typedef Tvec4<int> ivec4;
typedef Tvec4<unsigned int> uvec4;
typedef Tvec4<double> dvec4;

template <typename T, int n>
static inline const vecN<T,n> operator * (T x, const vecN<T,n>& v)
{
    return v * x;
}

template <typename T>
static inline const Tvec2<T> operator / (T x, const Tvec2<T>& v)
{
    return Tvec2<T>(x / v[0], x / v[1]);
}

template <typename T>
static inline const Tvec3<T> operator / (T x, const Tvec3<T>& v)
{
    return Tvec3<T>(x / v[0], x / v[1], x / v[2]);
}

template <typename T>
static inline const Tvec4<T> operator / (T x, const Tvec4<T>& v)
{
    return Tvec4<T>(x / v[0], x / v[1], x / v[2], x / v[3]);
}

template <typename T, int len>
static inline T dot(const vecN<T,len>& a, const vecN<T,len>& b)
{
    int n;
    T total = T(0);
    for (n = 0; n < len; n++)
    {
        total += a[n] * b[n];
    }
    return total;
}

template <typename T>
static inline vecN<T,3> cross(const vecN<T,3>& a, const vecN<T,3>& b)
{
    return Tvec3<T>(a[1] * b[2] - b[1] * a[2],
                    a[2] * b[0] - b[2] * a[0],
                    a[0] * b[1] - b[0] * a[1]);
}

template <typename T, int len>
static inline T length(const vecN<T,len>& v)
{
    T result(0);

    for (int i = 0; i < v.size(); ++i)
    {
        result += v[i] * v[i];
    }

    return (T)sqrt(result);
}

template <typename T, int len>
static inline vecN<T,len> normalize(const vecN<T,len>& v)
{
    return v / length(v);
}

template <typename T, int len>
static inline T distance(const vecN<T,len>& a, const vecN<T,len>& b)
{
    return length(b - a);
}

template <typename T, const int w, const int h>
class matNM
{
public:
    typedef class matNM<T,w,h> my_type;
    typedef class vecN<T,h> vector_type;

    // Default constructor does nothing, just like built-in types
    inline matNM()
    {
        // Uninitialized variable
    }

    // Copy constructor
    inline matNM(const matNM& that)
    {
        assign(that);
    }

    // Construction from element type
    // explicit to prevent assignment from T
    explicit inline matNM(T f)
    {
        for (int n = 0; n < w; n++)
        {
            data[n] = f;
        }
    }

    // Construction from vector
    inline matNM(const vector_type& v)
    {
        for (int n = 0; n < w; n++)
        {
            data[n] = v;
        }
    }

    // Assignment operator
    inline matNM& operator=(const my_type& that)
    {
        assign(that);
        return *this;
    }

    inline matNM operator+(const my_type& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < w; n++)
            result.data[n] = data[n] + that.data[n];
        return result;
    }

    inline my_type& operator+=(const my_type& that)
    {
        return (*this = *this + that);
    }

    inline my_type operator-(const my_type& that) const
    {
        my_type result;
        int n;
        for (n = 0; n < w; n++)
            result.data[n] = data[n] - that.data[n];
        return result;
    }

    inline my_type& operator-=(const my_type& that)
    {
        return (*this = *this - that);
    }

    // Matrix multiply.
    // TODO: This only works for square matrices. Need more template skill to make a non-square version.
    inline my_type operator*(const my_type& that) const
    {
        ensure<w == h>();

        my_type result(0);

        for (int j = 0; j < w; j++)
        {
            for (int i = 0; i < h; i++)
            {
                T sum(0);

                for (int n = 0; n < w; n++)
                {
                    sum += data[n][i] * that[j][n];
                }

                result[j][i] = sum;
            }
        }

        return result;
    }

    inline my_type& operator*=(const my_type& that)
    {
        return (*this = *this * that);
    }

    inline vector_type& operator[](int n) { return data[n]; }
    inline const vector_type& operator[](int n) const { return data[n]; }
    inline operator T*() { return &data[0][0]; }
    inline operator const T*() const { return &data[0][0]; }

    inline matNM<T,h,w> transpose(void) const
    {
        matNM<T,h,w> result;
        int x, y;

        for (y = 0; y < w; y++)
        {
            for (x = 0; x < h; x++)
            {
                result[x][y] = data[y][x];
            }
        }

        return result;
    }

    static inline my_type identity()
    {
        ensure<w == h>();

        my_type result(0);

        for (int i = 0; i < w; i++)
        {
            result[i][i] = 1;
        }

        return result;
    }

    static inline int width(void) { return w; }
    static inline int height(void) { return h; }

protected:
    // Column primary data (essentially, array of vectors)
    vecN<T,h> data[w];

    // Assignment function - called from assignment operator and copy constructor.
    inline void assign(const matNM& that)
    {
        int n;
        for (n = 0; n < w; n++)
            data[n] = that.data[n];
    }
};

/*
template <typename T, const int N>
class TmatN : public matNM<T,N,N>
{
public:
    typedef matNM<T,N,N> base;
    typedef TmatN<T,N> my_type;

    inline TmatN() {}
    inline TmatN(const my_type& that) : base(that) {}
    inline TmatN(float f) : base(f) {}
    inline TmatN(const vecN<T,4>& v) : base(v) {}

    inline my_type transpose(void)
    {
        my_type result;
        int x, y;

        for (y = 0; y < h; y++)
        {
            for (x = 0; x < h; x++)
            {
                result[x][y] = data[y][x];
            }
        }

        return result;
    }
};
*/

template <typename T>
class Tmat4 : public matNM<T,4,4>
{
public:
    typedef matNM<T,4,4> base;
    typedef Tmat4<T> my_type;

    inline Tmat4() {}
    inline Tmat4(const my_type& that) : base(that) {}
    inline Tmat4(const base& that) : base(that) {}
    inline Tmat4(const vecN<T,4>& v) : base(v) {}
    inline Tmat4(const vecN<T,4>& v0,
                 const vecN<T,4>& v1,
                 const vecN<T,4>& v2,
                 const vecN<T,4>& v3)
    {
        base::data[0] = v0;
        base::data[1] = v1;
        base::data[2] = v2;
        base::data[3] = v3;
    }
};

typedef Tmat4<float> mat4;
typedef Tmat4<int> imat4;
typedef Tmat4<unsigned int> umat4;
typedef Tmat4<double> dmat4;

static inline mat4 frustum(float left, float right, float bottom, float top, float n, float f)
{
    mat4 result(mat4::identity());

    if ((right == left) ||
        (top == bottom) ||
        (n == f) ||
        (n < 0.0) ||
        (f < 0.0))
       return result;

    result[0][0] = (2.0f * n) / (right - left);
    result[1][1] = (2.0f * n) / (top - bottom);

    result[2][0] = (right + left) / (right - left);
    result[2][1] = (top + bottom) / (top - bottom);
    result[2][2] = -(f + n) / (f - n);
    result[2][3]= -1.0f;

    result[3][2] = -(2.0f * f * n) / (f - n);
    result[3][3] =  0.0f;

    return result;
}

static inline mat4 perspective(float fovy /* in degrees */, float aspect, float n, float f)
{
	float  top = n * tan(radians(0.5f*fovy)); // bottom = -top
	float  right = top * aspect; // left = -right
	return frustum(-right, right, -top, top, n, f);
}

template <typename T>
static inline Tmat4<T> lookat(vecN<T,3> eye, vecN<T,3> center, vecN<T,3> up)
{
    const Tvec3<T> f = normalize(center - eye);
    const Tvec3<T> upN = normalize(up);
    const Tvec3<T> s = cross(f, upN);
    const Tvec3<T> u = cross(s, f);
    const Tmat4<T> M = Tmat4<T>(Tvec4<T>(s[0], u[0], -f[0], T(0)),
                                Tvec4<T>(s[1], u[1], -f[1], T(0)),
                                Tvec4<T>(s[2], u[2], -f[2], T(0)),
                                Tvec4<T>(T(0), T(0), T(0), T(1)));

    return M * translate(-eye);
}

template <typename T>
static inline Tmat4<T> translate(T x, T y, T z)
{
    return Tmat4<T>(Tvec4<T>(1.0f, 0.0f, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, 1.0f, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, 0.0f, 1.0f, 0.0f),
                    Tvec4<T>(x, y, z, 1.0f));
}

template <typename T>
static inline Tmat4<T> translate(const vecN<T,3>& v)
{
    return translate(v[0], v[1], v[2]);
}

template <typename T>
static inline Tmat4<T> scale(T x, T y, T z)
{
    return Tmat4<T>(Tvec4<T>(x, 0.0f, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, y, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, 0.0f, z, 0.0f),
                    Tvec4<T>(0.0f, 0.0f, 0.0f, 1.0f));
}

template <typename T>
static inline Tmat4<T> scale(const Tvec4<T>& v)
{
    return scale(v[0], v[1], v[2]);
}

template <typename T>
static inline Tmat4<T> scale(T x)
{
    return Tmat4<T>(Tvec4<T>(x, 0.0f, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, x, 0.0f, 0.0f),
                    Tvec4<T>(0.0f, 0.0f, x, 0.0f),
                    Tvec4<T>(0.0f, 0.0f, 0.0f, 1.0f));
}

template <typename T>
static inline Tmat4<T> rotate(T angle, T x, T y, T z)
{
    Tmat4<T> result;

    const T x2 = x * x;
    const T y2 = y * y;
    const T z2 = z * z;
    float rads = float(angle) * 0.0174532925f;
    const float c = cosf(rads);
    const float s = sinf(rads);
    const float omc = 1.0f - c;

    result[0] = Tvec4<T>(T(x2 * omc + c), T(y * x * omc + z * s), T(x * z * omc - y * s), T(0));
    result[1] = Tvec4<T>(T(x * y * omc - z * s), T(y2 * omc + c), T(y * z * omc + x * s), T(0));
    result[2] = Tvec4<T>(T(x * z * omc + y * s), T(y * z * omc - x * s), T(z2 * omc + c), T(0));
    result[3] = Tvec4<T>(T(0), T(0), T(0), T(1));

    return result;
}

template <typename T>
static inline Tmat4<T> rotate(T angle, const vecN<T,3>& v)
{
    return rotate<T>(angle, v[0], v[1], v[2]);
}

#ifdef min
#undef min
#endif

template <typename T>
static inline T min(T a, T b)
{
    return a < b ? a : b;
}

#ifdef max
#undef max
#endif

template <typename T>
static inline T max(T a, T b)
{
    return a >= b ? a : b;
}

template <typename T, const int N>
static inline vecN<T,N> min(const vecN<T,N>& x, const vecN<T,N>& y)
{
    vecN<T,N> t;
    int n;

    for (n = 0; n < N; n++)
    {
        t[n] = min(x[n], y[n]);
    }

    return t;
}

template <typename T, const int N>
static inline vecN<T,N> max(const vecN<T,N>& x, const vecN<T,N>& y)
{
    vecN<T,N> t;
    int n;

    for (n = 0; n < N; n++)
    {
        t[n] = max<T>(x[n], y[n]);
    }

    return t;
}

template <typename T, const int N>
static inline vecN<T,N> clamp(const vecN<T,N>& x, const vecN<T,N>& minVal, const vecN<T,N>& maxVal)
{
    return min<T>(max<T>(x, minVal), maxVal);
}

template <typename T, const int N>
static inline vecN<T,N> smoothstep(const vecN<T,N>& edge0, const vecN<T,N>& edge1, const vecN<T,N>& x)
{
    vecN<T,N> t;
    t = clamp((x - edge0) / (edge1 - edge0), vecN<T,N>(T(0)), vecN<T,N>(T(1)));
    return t * t * (vecN<T,N>(T(3)) - vecN<T,N>(T(2)) * t);
}

template <typename T, const int N, const int M>
static inline matNM<T,N,M> matrixCompMult(const matNM<T,N,M>& x, const matNM<T,N,M>& y)
{
    matNM<T,N,M> result;
    int i, j;

    for (j = 0; j < M; ++j)
    {
        for (i = 0; i < N; ++i)
        {
            result[i][j] = x[i][j] * y[i][j];
        }
    }

    return result;
}

template <typename T, const int N, const int M>
static inline vecN<T,N> operator*(const vecN<T,M>& vec, const matNM<T,N,M>& mat)
{
    int n, m;
    vecN<T,N> result(T(0));

    for (m = 0; m < M; m++)
    {
        for (n = 0; n < N; n++)
        {
            result[n] += vec[m] * mat[n][m];
        }
    }

    return result;
}

};

#endif /* __VMATH_H__ */
                                                                                                                                                  startingcode2/readme.docx                                                                           0000600 0001751 0001751 00000403412 13157110341 013532  0                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     PK   0K               _rels/.rels���J1�{�b�{w�UDd���ЛH}�����LH�Z��P
�PV�3���#����z�������a� �u����M�h_h��D��b��N�
F�����H^�#�r�s�Z�1
Kv��c��\SdY���D������i����ɢ=�ϚE����}PK�r�D�   �  PK   0K               word/document.xml�\�r�6}߯@�R����(ɒ�a"e=�e\�q\�fv��APD Z֦��'�K�^t���ڙ�G�I� �ݧ����?���P��H�N��rM�X2�;�Ǎ})�� s�о3���a���^ H�D#��(O��L&�"��jČH�D�DĞCFhqs���DZ�����"�	ԅB�Xã��y���[n���u%�X�*b�*��l��M��v��|u*d�JA�R D���Ƙ%�v�:U��>_$�.|r��ürNQ�!Y��6
�,��n�л�pJ��&��v"E���bric,��� ��F}ƙ�Y��L����j���3��t��}!Ý3����`f�K{��3N�Ի�����㚚�I���P��d�I�D+�Ɗ0�w�,�
��)�1�z�A���u�5w�nŚ\��u�������#j�Ԡ��"��SD�b|���� �����<�N3��s8�����s����%��'��Zp?)0�=�b��JjEwhI�/�)�IJ4�i��\%[� ��/`[Lʔ1�-��N���X��ߚ��Z1�h�R�Yr��ǂ�#O��s��%$N��P=�^��ي�n��5R4MI��&p�4�H�|U��h_zн�b�Z?�>ēL"�}9�k=D�32�����yH�5$R�\L�����/��:3z�Ҷ�������A�5~Z�K׸��g�6�z��������6a���᠛A�e�Ipz�:�0)�)���-�'c��F0pns�2��y�J}��M���dd/�:ᙆ�A��S���a��&2��&I��ı��Bx��,�[L�8�v�v����\C�iﶞ7����F��..~�:?��7Q�}��Ͷ�x_���u��c#�(�,U�R���+�J���~xY7 ��K{/wx�ͿD��R9�c������N�
�TBS;t=�J�I>�� ���J84D�:��$�f��=�S=C�y�[#�[�a����p��=�:$�wq����	�`�i
b�	�����*��҅-)���e�� �4_�q�F}�)}��F1��[�S�"?3���)�D��؍�ԫ(�[KhV�"�R��9e{_�=�"�;D�,N��͏a����t�[ ����1��D��
�K<�8�V	7z��no�O����0������;ݞ������k�{�P4a��m�Y۫�W.����%���c��w8��t�$�\"������i�'���trNmCۅ�ݜ)9������d������*�����2C3��JA\ß�s�������C�1�${ ���I
Ԡ�[Pz4���YM���ګZ�;�$��(��]�篤ӈ�@�JX���aˇ�n�`�Hz4�i�_eN����WZRM"S�˅��l���]&i��?�O?@��wp��U�m(csa��
�ZǕ;�J�O���) ���%�oF��lR0��
��-�h�9��܁Z�Z��z��z׮5�5�5�5�5�5�5�5�5�5��v�l�i"��a���k���{�����6G�-v!�^���`���K��pO�Ԍ���K�k�\s���d-����(�aR$&��C8j�6g �� �����c����'�3@�M�H9u�Z>����A�D�l=�xK�z�'`+G��{39O����.2�f��
��'�u���gBS��l�
9C"��&g�BWS�:�-͙�T�dœ�
���:K)E�:!�Jۤ���{ڄ��WuB�VLT5�5�5�5�5�5�5�5�5�5�5�5�_�f���D�&�����Dص?�L����&K�i,)�
!Mp9i�46�bH��Ŵ
�Y��
�[��
xqz�B� ɧ)�����'wI���/9
�,Vw�Oe�u �U}�k��O'|�#��0���0O9�a�V��#f���uE9�d̴[H���!2��%���Ty�+�ٿ�u	V����*��
�U.!���"�2�*���=�ɃYUN�t1��#H���j>�н��E��}<K�Ȝ�#r"#{�(L@:QrFY"c?�G|��aJ��B�2��j�}A����
�n��Ao��fkpW׎��8����
Æ��f����q�N��v��c�G��_�W���PKq!O`�  y  PK   0K               word/styles.xmlŝ[w�:���W�x�y�q|ɥY�=+u�I֤iN�N�<� �:��R'�������͜�����E����x��!��;���ux�I_D�S����ѹ�$)�|ȈO������$}	x���Qr����4]_���!K~�k��2Y�����Xz<Ir|F�ǧ���ȭ0�	 �e"��o�r�ר\|x��
�
z]
��)[�5K�B"}хq�л�y�d�A^ۼ<������>�%˂4Q?����Y���\�(M��K<!��X�8��ș�X,����2J�8K��D��;�<��1/��W1�s%�@�$�9S�����;��=V�xt�m����:�ݩC���^���;%8(K>8���𗾦���� �L�R���4�|y+�'������=v���n�c!�٧�w��9ŵ�}�.�V���W<��pw��+���'�(�{|6��
�ӳ�׺�7�[�)�@]���͵�+ذl�&�g�)��!�1jD$��79��}��[�h�V7:y���Ս���F�ou�w��F"��s���qFD�1gB�9!�qΈ8�D��Vi�҃�����".���� ₀O���9Do".��b����n��iK)�H��I�s�rV1�&�N��$�$����{�<��ڳt��S�l9r�,�c�	f߂����a����1O�8���/y�'ܜҰ頁��e��6�쑌�#���*"IP�4�ҕ�A`�!�bI�Y|�I��R�C��uGcb��?7И������4�bP�U�4��*iD
Hք2��(����VX��Kh2�G0%�y���O�
���N
`8G��8G��6�
)6�
)hG���B�Q!���vT˱�Q��Q!���vT�@;*�|�P�P��Q!��Q!���vT�@;*D�"Ў
(G�V�
)hG���B�Q���HG��8G��6�
)6�
)hG���B�Q!���vT�@9*�rTHA;*D�"Ў
>�G:*��9*��qTH�qTHA;*D�"Ў
hG���B�Q����B
�Q!���f��#J�k�C����}�w>E���?�ޛC펪Jefu�ეON㇇�qw�XB�)��!x�ˬ����2]�R~����)�IWI0�2i3��$H�&m�^���I[��K�np�t�_V/���n35�A�-Z��a��� l��\�
·�'��t�~) ��c�pf&��%ԕqn���̄��3���L@�ӈ�+֌Bk،�S5t3����L���T
�?!����d|��+���ym�F}oxF��S�m/��e�h?���Qu��GX���*�c����l�j�0m-Т�UnR^�HVeQ���ﳪ�m[�˰"�.��ҫ�K��=�.��{�
�ر��Q�x:�X��C�ڻ��|�p�W�U9{]>]��|6����H%��ML��ŜՈ�D�X�`e�G7�V"���,ֲ�
>�\m��a��rN�>��E�(8�r
�����:2a:��(��z�-i��7����:o��D�7�����g��^�Fj��M��T�oN8pٻ��I*�gzҷA|��j󊢔]���w/@_ �;U�m�\�_A�$ѹ[|��`ܗ1T�	)�7�Y��WG����+��]��%�/,���y�k�E}:z�+x{�O㰾>�T9^�kVӝy9���BPg׹�����)?*���z�]���O��Ҡ���iY<k>��*}K�6��V�R�U����8U��;�b0X0ߴN|�%�𦩮{�:���;��j��8����/����p�x$���nӯ����<٥��XƑ���f9ytpQ�!g���Γ ��"�*5N�����S�'\>��V�m�����H}�e�J�5�T���oއ��(��� �ޠvI�-X�cT��O�^4��n
��'S��
�fE��KQ*���1F�Qe���N(\"O�O/�d�{>LZV����g�ݎ/b������1[��Z�Lu?��� jd�3kM�R(����]$a���C�f"�Y�]�o�7��X���ݴ>��G
s��/~���6��_��D�Nj�V/c��r��$Γu��i�Z<~��f�,t$x�w��V�S6ޤ����(!o�.Sd��qk���T�b*К�t
�����2v�����o�����
���L��F����j*��vk5��N<o l���Ҿ���r6ɋ��c/_)�q�K+��؇�KP[N�z�'�Q
;BD,8��E ��-�
)D�:���6�_b�'��@���I�S�s `	Q�il��˩/��y����}׌����L�7yfp^JQ���{B�8jgr亁��憜�O�rK	��{T"b���������P��텐;�_�Ý�pM{=�
� �]�H�6�+�D�
���i�ͱ?��6Ouþ?�s _;{����y~L��OлV:�
����X�ّ�����	�R�G��tK�)
�J�
�w
���Q&�����f�ܓ��oo6䗗�ұtU�*���c�+����b`�u��d��Qښ�%?.��U��t��rB�W�m"�(��
ο�7����K�X��+��#�B�)YXEI��T�@Y�v��J��a�ȃ�H���AW��@�����Ts�B��`����%�貪��LJ`��|p���q@�#�ň�K�f�b���^��i�����d����D_�mO�'��y��hظ�\sx[������w]A��SVg��,P��P�}k��9Q�ɵ\iKݾ�\8�tf��I��d:�h�,���F&��e��f&���U4�ѕXm�o�x�i���c�#d���h8���ԧY� ��A$���{,Ɵo8~x�}>A"�RI+�aj���;�\�X
I͔��h<<�UF�yDsSg�(��@PP|O�eҁ��>;��J�$:����\�u0�o�uP#�6̾�@�E�|P{��-�9Pn�I���f҄Ɵy�
U?�+z�֙W��x;�Ӻ[��H]2Wu��"�쑦I�ၩ�}V��za�B�m�u�����</eA;u�DIi�p���=�zyYi�o�[�+�"�(�W-���|vHە���h�7���G@k�a]��ᐙ���c���ḫ�5���? �
.BFV������]�����(���j����W��^��`M*=
*}$�V���	ijD�4��45ێ�T���k��(k�龘z��XS��f����ީ8����l�{�(/.v8���Ǥ8�߸�:'ω�W8_�B�3��O�%F�*�c���L�j���EO��o5ܬ���abd2�jww����&.#����J�y�n~�u�$�I���J�F�M��rec��_�
�<Ѵ�
>�ﳟ\\o��IF�=��Mz�ʎ�~��9]�I�3�{Օ_�vM~14.\abd����oh%�b�]u�ԋ.��2��\v�%�(�����rdI�p^ �)�\�,y[KwM��+�LAi��Tל��_� q،��;��c�	���%�I}Z��?��ً�8���z�,�C�|�IM��.� �}�v��$�
�M�ꡯp�B~��zD�$~��B}V�Dǡ;(9x����ܞ_�d�w�*#6����#����%n�:\e�����h�͊�UR@�x�X�[-F=�0�GW'ѩ:lRA������Ѕ����k۟�W��4�Y�ߚ����1>�:�����?�9-K�{��J�������T�:=5U�	Y2���ANN���C�O�lX���GcyU��Q�P��@�A��C�G!�����E��p<���TPP��!�򲅵�b����zG+�ߩ�GU��a�Lf���Z�k���P��#d"!�XR�wf�>�j�.�F���rK���

n����M"O�3����88�����T�GD�+J��Sȸ�.���OLt\�F�'}E��RW,7�Y�Q���x\������Y�*��}�I����52/sr�y�h^�S�xz����63��b'0�9չ�f���s�9�Zd��[Y|rvuuq��=*����GƲ��*E?����bg'��F��S��zK���F[�.�7zz��~:Q����^W��m�ӵ---���{cF1Ԯk�� D\�#��|'�SxE�u�^b�:��҂����9�<&�g]d5�� �PB�{�7��`���$��뗐��vm�:�w!a���[��]oKoD�H�f�
U�%��r�


�.Gt4A��:?��Z[/�"�2x=u�p�s�mt^W�4�}ߤv,6 E��k����T��QEB*`���V���K��O[��t���
�f
X� �Nsll�%��^.z)?=��S�NA�tx�5�B!�J���SRB�N¢SJ:��)����rE�������O�g��7��#LT/À,>�9�7�q+++HH�Q����SC7'j�k3��������>��������Ӿ�}ߨ����=x�hi��[3頹���O�I ���RB�'�[w����_}r�z8DJ;u��t1�

�;��d�e#�(XJ���!�aQQo���Q��e�g�Դ�P��˿�-p�ki��|&���`eic2We��5�����C�ϯ�4��TMII���S
k��\s��b�c��E��{Mđ:ժ9��k�M����u ����J��B���~רP۷�������w߾}c�~����B�|¸WL������_�Mv2yݷ��fH%t�$�įL��LR���V� Iqz��8�w,M�K�@ٖ�a| +J���h,�h��p}�x����������$]Wrg��U*NNFN���'x9\�h���ts�K�M���o]���Z��%��j�֪��ά���˞R�aߦi2�Wz&1�R�[�4n�֛�1N���O�22IwWQeх|PA�)����m�ݵ��z�-��9�V�����욖�'�6_��rN9��&���R���'���������=�������pddd���i�{C���� 	�:�ttt`��qT�X��7n������'&��>����9a2S+ܟ,V����鷺]�����W�.�^gG�Ʀ�/��Y�V�g
��[M���:[�|�T�a��]�}��~dtf��$�3���j�!"��@7WT}���f�8����$--m�(]�󇾡�'W�� .��\#���n��~͖X�>U��ߕ�>mj�ۯ�͞�p�p+��L��_p�=����S�Nr�����]r�#7�nGkvlz�޿�k�ɨ6ح�&ҩ�<l��8�����ͤw{8-Z^~������S�U��������5���!�[X����a�I3Uw��
�U`R��,�Q}3�,��2c秧�Psss�
Y�,�����]��3j=��a|�>�-��o P���.�+�2���^��mSmRQG�Y�ק����W�9Y�D�urL��b�7sc�k��$ߪHC���� }?w�}���1ɇ��bTPu�xgk}�_wXAz��\����.���Tg��2�܂����h�I#1���ĨV��fjq���DҔ�ǐTj��P����1޷�`���SJ\�V�Ä�r�W�TSs}}ڹb3�PMQ	��k������f�[���Ƣ~8�'�D3�󤵵ӇMv$���������^hP�-/)�m�A6����U#R��������:�PW�??G������yR�FmA�+�7U[�K����E?��#���s�sI��611Qm��{���{\\dj��?�J,����C��ԝ	$e��� �y�=�o� U����F�������
3+��b����*�1���J�Ǖ��8_�EQ��)��{�)�L/�Q-��M��g���ӛ�/6��d�l������wP�N�'TJ�rN�N��PqyBB�������������-W	�"YXd�Ź9�_QFM�	h\�233�F&�1v!�_��
 ����4ACGEE%��X.����èF!���;������v��pZT�ux�I���ۉ�:�|�3j������}�WB�
���T��`����n)���w�.��+c��EceT�����g��ID�A[�����/)��T��蠣��(MHN^IA]]�))�\++�V��Hx,QQ�nGR>>�H�67DY?�iv0�fR8
��ʏ&1�0�$�4���R���   �������p~��7�IWR2221�_�x\v���6K�|$%S��̧g(ñ���Q�X��*����WAp�'��+!��h	�u �i(�d0�Lu
��q��ݥuu��m��444R2
芥g/�]@֩E�Z
�~8w�MgK�V��叻�
TH��i�P!�"�<U�jHҿ�׽%2B<�d;Z'z���g�NNק��Q��+M�+�Z�S���%Kn�VBJA�)��է+o�?��8��U���v������"##�������^���B�G�*!��z�#6HB��o-%���u�<���v�������K�q`~=HØH�%f�]�>�VQ��F&l�^FU�s�H�&x���Zg��1�[�������zL��g}r�e���é2;rb5UUZ�W�&�
��z~4p�ݗ�bR���R/�.���l��J���[�7�{�3�^f1]��T�K�\f0����p�<]��>�����<w���@�3N_�P���Z�Uu�o�����W�p�����KL������A!E�i8��wJdU-<k��ÔEy�����6���� '�3%�|]-�j#�-�����E�|���z�$P
~.]1R�
E~e�;0K�[�wb�H��>�53�}���pʬ�S��}h�a��X���.�5���l����ꙣ\7#32d�:G�Ǎ��Q�Q*�(ʤ����2|�ak&C�*�� ��b�!�ͫ��"�ϥ�%K�N���K���f�(f��t����CmF���r�� ҩ��Z)'�{�z1�Y�h�!���5��A������Vr�#T�&ڋ�1���+0gV[�&�TӅ�@POJ'I��`����p�˶ڢ��~��v�����#�J���A{;v��M�c``�3Mz�A�Ė�~�+�TUU��Ҡae�i(Wdl[�-��3b�������m2���D��YYU"����.݃�� ŧڸx�^��g^���
��2t�:H�)<b��{�E?�UL�Lt4T.�Ev�rW�X�e
zaL��¥T��$�����֞�d�%l� XZ*
�B��k��<A!b�(
�<K%}�I����Psq<\���{`����-��a����t�,7��Ϥ$3�ۆ�S9�z~`�_.*&c��L���!��`�-����n��������?����AK���Z-;�6����j��g(�L��<���iUY�&�H�Ǝ��Z-�+&u�0�3<]#KEa!(݌_^���Dw
�y���֚@���<�hNn�� /��@�)�uL�䶭h�y��sI�z�ʹA�,�#�R
��瀨�i���h�f�1Ĩ�2�560`��+����a�������~�l���^�$��K���󝵠`W���������1��H����4�|� )	؏"�	�v�:���m�"�ll���È���q	u���am�ӽ�Ⲫ�����
�FW����`gNA�NďK�?�
�b��r�}	�c/٬�œ�UHc�;�;`�agc�%1Ub� ܟ�De2O�"�Rn��+��X h��h�'�����U/d_��1F����5�ds`��RHp=p���n��?�4����|
���
1�xH,Sp��Kq6A������Bo�I���6c&ϗ]���8۶/���̜V��,��Zv�@�ޜJmf�h[< �{+cMA��
�!/��ڔCddp�ڋi�K<8o?}v���~���u${/ӱ܆S�A�


A&���kq�:�-F-[�3	
�;����D�̈́�	0U���z<W��d�n����@(臰rj��p����t�xY	]ply2���+�ge�jw��<���g���H�\�@ޔ0����	�	BJG��k0No����X{�@�^��2f������ܗ;�`.�����	kDD�p$�Ȉ�� dF�P�@W���3�����J�⚎�!x;��C(��yH��g�f3k]b/�ߟDRo�Q���z�#�i��b5�o��V��5#������5��2#��������g� ��G-X@ݕ�����]�	Z���t�>�6Ͱ�����qJ[���H�����盭q���,Sr��>`S�ٖ���(*
т+�)�l���1:�d�^w
z�d/:����YNu�	l�Pa,�n�����/�(�������J۸,��d����'(.��Ux�+�I���JmBނ�*f8,�^H���nPJ�V�C_^^�)�z�u?_�|[tgP
W����):T�����Znf��r�{0,��7�y��Qk�v/��-��bR�ccc��^^6���4;�1�40�1�SPP@D�b3��=[�bd<�)�Q)�\,mnk[]]���u�����z��L]_��E][O !tN�LWUI&�	��J�*U�q���X������Zh@��!I�Z����t��钡��LU�O�LyA����.���0�j��!
�B烉�ϳ�Ө{iuu7 @�Kc�D���ష�����oF%Qm+����R4.��1��Oo0"Y�.::��G�E&�P�43s;�"��������aMYQ�d I���@�ֽj�,faeРo�"D-,,�!b�~�5�C�>�PEuss������8>>�?� 6;ls����~r����ܳ���x���s$���6ӓH����!,>�h�_��9n��>�{0 ��n<�����P���C]�E����sح���\drX' N����b�d (>@��X����	Z�<�A�؍6�h; 5d��YS7h��o,tt�
O�ëa�����H/<6c���w�X�-rjZ�̍����d:޿��.��b�Q�Ε_�Qիt�]@��*�w��9���O�SU5!;��*d���]2^9KK��%0K�!ϗ�� ���pX�1D��!����ldbb��HMN��J���!����Hb'R�\�b/FF>B�;}��y�3-��b�^^�''D�~k����{W����l�&ŹI��7@�؈���]-*�ҡ��{����IIXC�N�nn U�ا������:g&.u�/��f��|0!��*��q�ᦦ�`���8��
�*�l6����m&2>����,�8D�,�8�a�iև�6X/�v996�a�E�&��Z������J9��~N_�p%
v/��������e�/�NN�����K��Lf�b����S�����(�^���z��X��U�Zz�އ��>CĤ���VG^>:?�H.}��V��

ı3�
Y|�A[JN ��S��Ĵ�M�w��KQ󎷺A,�ӟkLWW�g�_W���
��x�'7;�zo$�������`*�\�� T�)11(�����f�A�+�6C`�	�z�����D��*�a�XeYQ;::��>��4PNE%%HZ�
���������
����^�{D<�qJ8�[�DR��1�U��a737o�?"����3/����t�zw8��?!g���iNNI�
I��ޏ��mR;�% ���p	}�  �1ux�́[���thbҭ��p����)\[�/[a��>��BD-�R@LY�R�s'qr����0���)�Xg�dsaq� �7rW����bvo	��@~y9��kD�'�Y.!�>�����Uj5G���q:���X=C�s�4�k�����-�z5K�g�1����3��V)),����G ���'�M��e3|����띱�۱ZZ��5�%.*���b�It }w��[������mkgw9T�?��UM���L����B�˳��jB�G]���0�bG�\H��מ��H*ˋ���m���KU�����#A����狍�����^_�S,t3���� �Y����9��AZ����p��U�
G�m�H�a�t�y.(��)�Ǹ��f���+~���'�	8e%nO���FH��[OfK�,�'����'�i2Wvî��������H�������>�t�YE���
#���[���j�q��lnu�w�����|���2�	�� ����YG�� �Tnh]NI; ��#���n�O�Z�ys[\ � QO�L��z�3 έ�����".hPP�ݾ�M&�Q�YW�D��vmiH$��U7" Sf����/'ȵe;���(]��SӪ���T�}ճ]X>�
u�f߈
rrf��%����[��4�s(" ��NO��)�������h����N��ȇ�xniye�.d����1�h����=����Ks3�#���b�nʑQ|[cr#��)%P��l𗸇۟?%Pe	r:߶J�E���kk�v�W8]f�c��ɇG�L�Iꤦ�Jf�x�>��lw3�-���t�&.S��(Y�D:�4(88/Y��0��-ގ� �K�|�����˫P2��MJ*����aX�P���Kq))l�� ���bh���R��1�s?����h	�t#���-���,�Gݒ���Z|ȗʯt�Y`o����j��@���Y��&O�K�O.Ar���Q��!�>?V�@��\՚+�A�i_�(괮�k#�p���nT�?i�������H2�ӻc���|Qٜ$�w���Ϛ�̸>���:���:ikD���9��;.��L�Tkw������* s�յ<{���"@J��Y�3��ta�`v����{?���:8���
�Q�fw�P:�/9�ɬ{����lQCG��>��������eЖޕ�o�c6'����*�q�@�\
��Ta����-���o��+�^�R��4�u�
Y>+�S:ʷ��}
">�/+�e���D@@�;[���d��@�/�7�A]Dl�K@Q��]�N9=��?H)�|{M�V�A���/\p�`��X�����G{a�߁p�'K�M��?� ��f.�G��@�kU�6X .Tm??\9=U`��۩6�b���p��,/��η}T�y^���F��
���=Uq�Ơ�+"m�Խ��Ũ*���j��v�xf��*x�u������s }N���x[{j�?X�`q��wc3=�)8��������Ȉ��u�y7��}}k歀ַ�� �x�%r�y;�;�E��>l�l!���6L~B��Q-�l}_��dc��?�4��b��1]n��aL��D�b�����n�5Q�mH�������0�(�Go���0�0����J!����P�^=���'5���gb\|`�lד%����5��n����-���F���?㙾2Uuu�3E��D�+��H�ƣ��hU�3����..� T��Ƿ����b;�O����^��2�+}ȳ��@�ts����ٻ��())	�G���$I%3�.w~����**-�W���yVՏ�O)��2�c��˿�����i�S�t^�3��P��I���Z�2���{��c��qG�2㐃3��:��p��Xl�&B�����B*G��@��ELYY%1e��lʘ����
jh_�K�xFt���۞��H���,Yb\�����Nhb����ɼ���(�vɏ�,�X�q	2��fL��%*JJJ����{ ���m�py�B�(
	a�V)/�;����߽9Z���/��K��ם�s4m��I���?���
+o�	����3���a>@Nt�
B�}Hn�I�����aS����:�CB"
�!���t�ݙ��ԙ��z�P(�zz��Z�TI��؜.����DB�&hP��z&&%��(u�Ccc*4�t��B�Y�����#�0�J��~e��&����&+-_Q:s�蔠�0��iz�p�
DD~>\�03����n�\$V���^P����9'�E������$D?(���&b=Ѕ����sW���3����p��<�IWר.$$���}Ac��TSΡ��$t$�ؑ��jJ�R���3.���⯥qq�42����Hy�d!%��L\ؠ���=�碠@�{Ă���h47ܱ��Y�'���h����"qy�|��ҩ�������g�ۤV;;;�o�:�>���IH�p�=@�IJ�NTt8.٤�EG''>ξ�FGW7���mo���ɏ�GW�fv����{KɅv���]__�A����iϖ�60d�F��=�ϣˎ�11�,���j�T�&&&�w!.7DDHvM�U��-��uAM���l7���d�G�,쩩W�cw�kT�$7|:���pw�V����YA��gkOp���e�	��g�P�
000q�"̖��!�SU Fu6�;�^_:�Ī	���2���,t0�)Ǭ�,��ȶe�`���N�7�q�z< 7ˎ'k�H5)5�ѡ'���EOߐo}p����jw�X=j�|�,�����M1��lKӥz&j\�$yYy�5�{NA��5���_����G��&��u�Yᷙ��0�qͨ�Z�K �M���k�,�e�8��^DI
�����	
6��͞ ��w�~-��o�+��VO�Yh���
�
�\8ə\�y"˦���&:��ɽ{���!��}k/iD��
j�<D�Rr�Bh=bY� 	qq#�̖�}�}a���Q-��*%�+��;� O�;��Ti�ĺ�����s�n���x�������WYY����A~�����h��Xj����L8C-@�[�7q�Ur�_��S��%�t���;�嚜�����Dfh�����y c���1�Q��"�^E\�	Ŕ�`�ip�9��7m����UBZk�B/��$D�_uc�!� U��`ǭ�aРvwb/���KSYj��u�X�pF
�Ŕ�=�h�blCc���_K~����v�4��������!�T\Yb>,-����*J�0� bDޤ�����-�p �6l��x~%�z������"����p�^Z���n����-��� k5�5�q<�+ �?7 s��ߞ�T��K?^L�[�+�d�*3�9!�׸8�����|���o}����HX���tj�WzE!�N���4��!�5�u��AR��/�^��}�����qV(���MG�5�Wm+Z,Bm(`%?l����T���
raLљ�2TԨ�Z��''	9��*�=���������J��f�>�d�
�x�!�K��Z]]=������o8���h�XTD�;˫��
�m`�T���N �����~��;44����a�O^�7�jH�a?44,,�Z2�#�Ֆ��p��@Y^ZZ���6�m߮���J�������;m�!Uo��w�	����M<@R�V ;V'ڼ��Tk�M"�����x)R��_�����d�'Q����U�������y��P��c�RU���r��w ���+��O�z:4w�ɏ��.-���_BE�OxUl����2`��uÇ�>"�cR�K��]��*���ŋ�����CbPq�##��t��m�����J;t�=R�&��y�0W5�%�ĺ�����I�BF$����>�^9�`3\�����/��_xr'Վ�&�z�M����BJL�Hܬ��H<#�6S�9F����NjVQ}�9��*ք�x�(:Nc���E[t4���<<�Ka�~��T��I��|yq���'��.ԩ5͘��op��l�
dj$��;5��A��SbH�����VVҠ�LL��[ҥ$'K��8��_
���������?��y�TF�F�J��4�]��MFV�d��Ϋ���V��ͿKs��&�������+��0�ќb�PŐ��l<��`��z�R���5��Cq���Z����(r��s.x��.5ٙ�]��Im�@>��x1Y�Lz��q��tc�GU��\�&�;r�ɠ�ѝZ�B[���G+�����,J�ff|��Oj��V���~Z�����v���K�劧�E]��=n���a@Q�M��v��1��|ؕ���/�h���>�!�����ѥ�x�4����Ø\J�.�I��w����rR2f��:��}I)&�ů�B�˝��H��
4�|����֞��C?���z�Z��`�m���bbb���*ߪ��ק��HH�U(2�����^=�Ͻ����κ�z����������	lm��|��,dZ�s(*���}T���?�j;���G��Ro��i4[Pkmf/|!ZJ���7��l��]��p�c�����滻_��@<�k��.  �������OLfCB%���cY���f�v�8��<p.����B�e��.��
dՉa�H\�2�{l�-ե����(��+Wl��_ľj��jD�Ӭ��&����kJ}�$h��Ь_���w?��h<o���E�:�_�a;�f`�v9"W�ġA�6��Lf�UM6��a+X��Jg�f�*_�L�vx�I��L��B��f,ʺJl��+V�+��p�C��P��RuS+\M��?3��t����������z�q���Jh�D��9as!zS�٤����g�1��|%�\�������i��p�1E���������u.0��"9j��~;�~0chv1�-�rʸ��T�,�V2�v��W�����_?9�A@��0Bu|�w������d�I�#�t�ϐSǖ������7> �~�@��JP,_i�[ZZ��^V�l�h4�w�]��+++��kp�X|� ��?�z����w��
k������R���y�����o��ڊ �̡�ӱ�;h;��&	%		
&**����*8�WTE**��!\,n���kw�bذ��Ѣ&^!6����CR{�I���������3A�=���>��,/gT5�4l<D��~��]�*?d5ԣ�\����A���o�=�*'[��d�鼟MKN+l	q��>)B�_���#xR�ߙ��W���z�K�'�Q�I�齮�E��C�b��m��*TL���hh�:��Ʃ�E�&���?�g��1X1�f��x��~��`[�<��/R�媤�uD��mT���X��z�=������Tv�3	6�:��A�a�y��Ǆ��B>����Ȑ�[��#4#�,,����#j?�4,���#��-Tj/�_q��Woq�j6z��<�+K���7���6Ze�9i���"��,���H*<"��_�&2�18��������
M�(q�i�L�ӕ&�ӳ��ѡC��N �O�)�.( }��m�޼���m��zg9�H#b�|����v�@����}ȓ����,lVg_���љg���
���V.h,?[c���T�+4�U����0�se
2*���%S�+���QI�Ǘs��E���M����:6;)0S��l[������gjB#�*d���>������w�=�'��Q��:
�ϐ�����=���XShy�������:u�
��UQZ��Ow}U��vg��evxLj�
�Gdܮ���8����ڤ�D��.ϭ����1�R�2_�~�i��'�B�lI�h�27��0,l;(�eE�����2;�|�]d����u w ok����$�ĸ�kz�s�6BELh��	�\\JuL[����Op���_��ף%Y����p*s*��fs��o5���g���GD�e��4c���U�������]\�P~ �%�k�l���>�
���� 'F�PM쳛��##��?g�H���S�+l+��F��đ!��I�h�������d�����n�"���~�G$����������
7�k0�?�YY�c5���ț�����;���Hb�QI�SMd����v!P���T�i�Nu,R�(c���˔�K��hM����q4�mh�����t�zJ�U^ӑ�p�p{R�o�8Y�=�'��F��k��x�|7�ˑ��KHkx[�
�jY�r�p�A4�v��
�=���}����7�8�,�E4�/�D7!P�G��C�a�����~m��x��diꚇ|��B����gg{"��4���0Mj����G.S9Q�o ��gPo�W�~�^ |�=��k��,�x�)��>�#I���� !"[X�ytZ��bs�։���y�Ų;mǿ�#�6W8\���RRJ�=�W|�"��� �BE�ky[c�ٗ��KS���)/�P���:����l�W�X�p����� }�ħǓ����<��P���>%q�x%���'����ۇ��f�A2Ʃ�����:q*�� ;[�]�DNG�ɭ��ɶ����M��2����h�W��'�x7[Q&���uɻ���~��Ř�����]�?<��<�`�1죱Įp�9��$c�" �x�Y��Z{�H|��b�j����4�+�
_zG!m���f��)kYkF�	gw����E�yz6K���GQ����,���M�KR��T�0�ѩ篥;�2
$���2U��|'���n���klE������QMN��]�dOc�#��������a�&��5ٛ��0�БG�Lg
=�'5o~�bC�Sq�'EMY��ҩ��������jI�6G����ʎ9R!<C�5Sy{����V�z��/uA"z�L�a2�
��q;���N�6�aث�8����H�J�]�I��R���s)6+�-Ip��/��˞`�i��$�����G��.�oy�'/.Gv�����*��z���04V~��Q�M�b?��z�C��K�KK���F@���Xz=��㪳��}O���a��_�����u"W7�|ܡ����飂.�[%�Y��xE�L/���3�ڶ�7�1���Va�`_���%�=��(/�U�/(BM_��m\�k�[�� h������Q:w�����#6k��n{t�B��I���?���q"d��P�@�	
b��|��_�}�c�@a��@�z2{}����v�p�탳���p��mz��9૮:A�'/r^�rX0+.�(3����we�}{�����Xn����7�e%�/k�6PY-�<-l�:Ja#�Q��ְ�@��t[X\|�l�;h��І�~��~OQB
#��ER.��@'��y���ǀ�[�*�$yx��X��A==�y��B�����m�7a�aP;��F�u�|X��Wk�U���CJJ���9Y�Z���o�M���Փ���G��	��Summ�XRq���dz�

���റC�����w{��+ݙ�?�!��"�]2�"�+�ebr�X���h@��g7���p�_�y���4�*>?~�a��ehh81$u�os8S��7�W�R��q���D����=G�ynkui'�Uٴ<%�Gl����C3Iu��b���J�燫�k;������6:�a�4/��s(��UEEE�ɹp��KNM�����5(N�.7�;�(�����"5B�3ac�}ʷ�+�(�
>%��G�胃9��^V��)]�&�������wV�ug
�&�A����W���<�{��ve����i��ނ��({ߵ���[.u,a������ނfTV�I(S�v�'��4f|v Ar��_��-�ל���hZ����s+qq���3s�����4�c��w��K���%%%Nk�{#
�X6p*���T������m0A����
:#0���S��t�mct�гO`ۖ888�Y���OLI�>�z����+���7C+����`���65������:�ݟ())���4��>��9�>�7�?��m��+���0��/��i��9D��m��NsW�`^���[�qJy�Oš���|Nj�cDɹvw��)Bg���Fk>�����)M�$	�V��
���AgH��� +�GU�#-G��h`��lX�?�����jN@�\�c�r��Kc���� �
�N�����`�t�X.�o�*���@d°fŠP�o�Q�H������@h� %&^XZR/$����B�h
S��t�5>v���{fX�h���Jo!�M��=��ӿ�M{86	��&(律�4(����A)QCS�>U1�`�ҥu|�����a��{��/�(Pti�p�%�}r"�����F�b]�*b6���^o�5��\�Nar+�}/HR�0���:̂��70x�����'�����D�ܻ����0`u>�Y��A�ˈ�a�Ɉ���t�k�u��/嚸�x___�V�� R���F�e���~/���5�����$�X���*Zs����yXV���Mu,t�Yɲ�{R	�!��X���K\�iN-���|�ƲOFv6Wʥĭ%%����n�Y���צ�SD�̜��ٗRN�ۯ߮�ko�U��
ruuv�jv�]�������}`ppqq�!��kkU;� W<
�DD|{�ĭ��=
�7[/?�&��_���r���� ��'����
9����	u'���QA��C��)���lA�$���+C���"uV��Qaf.۬=�h[�r��n���������Ȝ�N������Ͳr��
[9�ٻ���4@�¢��Pg�8((��$C�������9P�{"t�bP>ʏ����l*~��u�o'����aG	�:b`PT{�[8���:��^2w\0���^Xzw�u��K�k��M�1>�ҧ�KEEE�V���'Đc�*��a�&R�>f�e5U�ooD�e�	g� �V@;���?�)�a�g�`����8�D\�X+�����"�M����¢���ѩ��t#�j�0�9!��!K�v�dc�gB��ۢX��l�eS���U��?���� �Mʫ�%�3Bv�����Y1@*G4��@r6i�R�����k��0��d�(N��egJ��Z4
tT1͢
�G(��cyeeHP�M�Hcyf�\���xx&�_p�9쪘0n�s���Gm�f�O�rkhh���|���q�UJ<����yh"/ؓ==��I:.�{�g����K�C�)�`8���1w{�klbBӁ���e�16!�V�C�u	.���vbbb�r]]][[PX��ih<�]��$��

7�ў4�C��]�����v����s� �8�[;��C w|��5�s��	
���
���w0M�5_P�:��wz#)�
`<�����UϺ���&@��)>T��w1���.*r�33N@8�Fa`bf�v-[����\��/r�\{�L�����b�Z>���2a,oH�qA^Y��2�����H�\�л�ov��Er�X\�eD��!33j,(�>�{��;����n��Å�:9;��=���9���� ����R���R��lZ�U��f�����9Td�v���6�ɞ$��4�s~k��S98j�t� �f���E�?-��ڗ������
x��J9w�l�i�l>��O�,k�t���S��F%�9y��=�m��W.**
�?������J_Yb">>3���4�8���66�??����v�M��ο۠�>����
�O$tz5Cv���%G���)y�c�vNǟ ��;� ț���� Tf�2=A@c�!CF��3GʵY� d���8����zy{��Ϩ�����9���2p�TWob�m�}����Ǳ� �M�j���l��$� �e3�vqv�����gg�� )Ϲ�VVV@w3�W�����z>".N׎v�{=.��� Lo=9###3��p�[�����!�.�?����+H�<{	*b[__�٥���?�v!�o&K�'5f+}��<[F�[(�1=�P���#��AL���K/  !�i��e�8�Ҋ��ءc����p<wd'�د �L�-(�_��y�7@J�?ɇ%�e�2�lj��"G�@v4feu��By/��Q��z���?�Sg����q�"vbl�iJJ\���|��� 47�F��R��I�w��-����QK�$P�77�<_�m1�ٱ�?M�?�ͅ�GNN.��	�\�c�t���������ːIb(=>.N(C�	�d5I�{3�T��%�[�Q����M.��ј�ΠE=�$>O��K�Њ衞M�c���D>���W4Q�G�?�I�􌩘FP���&��`8\j�gb*r�cO�Lz�FDڻ��x_)������F��OMX�(���;;a\�l�PN(���b�n��j�0��76��c�`��� x�ib���}u
 <ˀ-+/�Rd���H�hcB�{�0Y��A�\N=�#)#���z���V���AzN`�E������!�-(��0���H�	��
�����<�<37�M�e<d�e6�4����Wͧ��V�L�e��q.TaeS�N�*>���c�s
�q�VWWW���l�Rp\���?-WᾤUc���<�Q�G�&�iT�_���	�y�vޚ����#�A�J�-��O��j{@tP��1�9đC^��,�3ǑPz������~`��d.���3E��ϫ\�����D~�t�Z_�#��v�A�/
��7.I����I{�L��ݑk��
+is[g���;yk	ikɓ�(1�W��(�ɡm�D�_�βn�	P��k��X��]E���|EOo1��Ud
���^/���RJ��`�),�9A�����2�1�9�Җ�s~�P������%znV�ǋ�n�xZ�����b.U2���]�Lu��f�9�#�t�����W��1�<ex��q߶�%��l��n"���Lqyl""bZ��������(����H0f�}N�~��.w���y�Fp*�} ��(����AV�bhY.^P�^�:͙�+�C�9�����U��
�˳}�F1�)*.vpp@%6��}4>#��yi��䌉"4�����@�Y|X��Ȩ��N�Uޛ4V�|�������gv��ȭ��{K�K##rR(�+��`4D`�n0�������l/����އO��"X�����I��ʞh���?k@j�N�|*�t�D�����񄁚 �ȲRe������#�o������R�xh�����N|�e�g����˹�iH�ќ_��	��������
4�� �!!Pq������>��BN2w��7+�-c���F{�2�VRsJ�m���w ����@΃`Y�1�ʃ

c@�B&�����.|�}�2=�<66�`��.�vB������V�[M��n�Dx�|ǹ|�A���j��lonc�/�v1�ljl�"�hk /ON��3=dkb�mu�vu��)��EARY*��X���)K �G��bMU1�D%N
H��R��t���E�CFm�A�t�
�+���"���N�TdB�=����j�4		�Y|4&������l%5d���ޏ��<��3��e��m�����ǁ�^��r��B:�Lvc�$hH����>����.<?�H���;ĩR]�V��b����x���l�
G�%�p ��B[���5D��mW��PBׇ@���+ۅF�e̤>EEEb_	�)����'�}��c�yo���I��K��7��T^�Jtll�N�;�m?=#�_K�H=K�'���z*�<���7�i!j[���X<�Lr�k9�I�e��T��d�����o?[�������>91a��ޏr}Dl���5��|M�*�(���������h��AX� ?06_�~�c3O�.�譿����L���̚�ʂ���1Ȼ�ê������8��>�%
���?+�n��~�	CUGK���[`	8��*ni�J\J3=�!�T�|
jqv6��J'PLŚ����p�̑�_�[?�፾&���˖�������F���a�Z�'>Z���ۑҳ=�C$���������t�}35 w0�$}���������̣���Q�*�W%M���i�O8����b����-Hg��)����Z,��U-��0/*��P�ˌr��m\�4	?��ŭGl�i�&�&5�&���j�W��&�q�ǫU�)I�{6Ǆ�**G��$m��%8��3�����х���_WP��̄��|�̗>����AB�H������o��ȹ�0[D �q��(w7St�>&�E��|�@:n�y#���p�V�j���n�(�n�J�ܒ�0Y���\�YG��~UX���;r����Qb^��v�
��ϙᱵ�I����V���u����e�0��2ӎ;�������9����Lڪvn6�q؝*T-}IE���۷�����UJ�102sr.�y�	mE�o�D�c �K�̋-�D�-�2��߶�'u�?��3�^�Bo��S������H~
��?���RR������ž;�j�8::���ޒE���Lz�?E/��Ϊ}����>m��" �0N����^i���j�*V��Doq��dd��ˏ!7\��<^�X���
ova��������`3KK99N�3��r(0�|�xh$j9l�MhH?��q��b_`�s�ϐm���^x��Έ�*k�|2=l�
�2+k%�l�`��R�׸}J
Z{Y��eҁ\GCYh�szr��J��ϟ��� �����������%$$�\*�<�r��>q�A�
.RUV�BF�ָX,�hj������ѫ�� ;:��GRc���MĶw�%������ɽ�E=�PNS{�m��t54vvv\�w �gi�7'I��G1�@� �h���T@B��6���y�^�*[+��'�����V� ����t�7;��g�8�n�:�����"�j#w����?;=moϓ���_�߱uơZ�w����ҫLDDdP�}x�N!Q��*�yzt4Se:����g�I�T�\�
�(�J���C}}}�5��ڤ�Z|||V���ڥ�Z���]p��M5@/3��n51�W[�VTU�ADDt�>����s�pws{�Z
��4p��:�6:��p1{�|���jy8��^r�¡�$D�nO��a��r��a>�@N�����������,���j�>III���j?�w���T6c��q�:�n��CA@F��T���L3��	H�_J�8J�2r�E��<�j�&ޠcO�t=\h����G��+TH��7_(�h��()2����gۊ�h?�/���x�K���c"_�}��B@Pqq�iLL�Z�oo	�
����S�Hu�B߀r�<����f���?O��&)ѹ�6-{#=F��s�%Bm�
#ҫ��[8�	˥�8��h~���Dȵ?��hf 9��ٹ����:�	`�����<z(���`��`��1�/��SSb�
�����}1�XXX�ޙ��{1z�OU����!�E`ꔶM��z��5x��/��v�i��t�%Bel��Ī����(:91�%de��������t!�
O�ac��Z)��.-�d5kQ/�r����Wj�qGN@���u Ɂj���Ց��鶷��a�Ȯ�.1119���r��UG糕ƃ�kTTTt�!����ZP���S2��֪�����Bp�@5#�������E6�t�E<L``�}y|Ld]~{u��qW'^
O����FI0�եfA��޾�JqSϝ=��/N}�e�W*�����!�O���z�ccaQ22> 
lZ�_��C��E��dm!� 7(x��+L+��IU���Nlƴ�KH0�W�m�}�s���d����Z�y�����ׂ�|������7�@Έ�t��d�/���o����_��`�*�n�P�$㿾�n9_kuyv��+ywfc8�vު�=��7����%��C�	Q��A�=W*�
��N?��C�i�� Y'-���U�����#&MT��TT������ �nD����L�s�n}��a�i���������P����f������H�~O�g�w"�-�K�w|WR��G������?VFrbG֗kqFP��z�K�hMV
^Xq�� �nLGc*g�,��򴱻_2�#ܛ�5�@� � ���^�ggg{{�����B������@w�L
�.h��t���`�}�����)�<')cu�R���*�_��k����V�X�s)�+���'�n�́��b9�\Ą�մVԝ4���I��z�Ґu�j�33��E��͜�^*�������
 K�A�H�>V���C@d�nK�ݻ�� af�<%��@j�'*�,=NNNj�#&���1�?���&�VU���;B{���%B+�؄5;;Vu�i<^���C�I��∞%��1B��2���_�K�)��U͕��iAs[-�ї�Z��H� ���l;]��u�:�d�����x�4 �W�^�v����10h�ɍ�o���������v"O���b��;�_��}��������ʀ�"���Mȼ"?�F?�c���}�7;>t��)u���I'�����uR�C��	\vV��F�Ɨ-���]>�EEEX�� X�c[�v�jp�nV���ĳ�e#�>4�����gBAJJ�qǍ�O�����PPt��yx< ���Q\B)
I_XX���2��Ѐ����ʺ�v��M@X��d��Q3�l襸�_�&/������X���+E��n}��i��c��������߫W����.<}0�-�[�
�"n/	-�����C���"�T�W""J�<�H�Q�� ��[&1��<�U��M���H�����=:>������gW��Z܁1��J�94r��L��:m�X^ǃM�m����Ek��GG�2���7����=&ih䯭t�a��h-�T�����,ߓ]�S�d6���ظ�@rPx�q���\`P�~�����,el�$��:�e?}ڗ�g,W��J1l�-1�F��[F8^'��L�׮�F_�o/)�m�q0b+*X(�*�
v��Q�AO�<�P�`����ʒRgA�GI%Фр[�����uZ���sOG��s�Y��U��E56..ﵷE8h���(^��ss�4�� $����r!��?���
��f���O�H��e���P�
`�Zr�����9Y/[M�ݮu(�ߎ+C��޺s���N���:�w~HI�N��b}p���%%%�V3�`sss���Y�
F����Ru��S��$	x�L-�+ZD�S�zz�*WO�_^>�
�I))E {o.���A�|���;��%��M�aA�Ăɓr��r����as�)j����W���NG�e��x��?� ���ljQz:6v���D�&�wh.�����dg��#s�sG�����չ?M��4���=�O8��:55V����� ��CM(D�����Y�R�OA#EWߜ#�� �s�lB������|rԦZ�gQ�R`1��0F���U&?��G1�󢎫6v��~�+)�<���g	xX����Ʀ�_�@�XZZ�lYbf�R�5<|��˝��.�r R�����$v����q��GGD��B�'���yJf@��>��(8'�pܲw�xh��6L.T"U�����'f�g�]�P�ǘ,���z��
��,f`��]@�᛾���eg+!l�(`�Y���T����T+�ޤZ�G����C�(x�K��|m:F	D o������gR��ݐ���/�e�{����<��{�SŚ��|�a��}�~w���*2H�*�e�"K0DZ�-���7;���Ζh�+�����|:�'��r�ёmK�(��W��ɠ����Ť���5������1:e�OF
\?!yEE镕�^9��л	��!�+�����W����ߓo��R�;����TZQQa���̲e4Cr4��<��Զ�U��YVb=RK�˹��cӫ��bΧ8%ь�7�~�S��xj9\����,�
fږ=�$2�4��$�Ŝ�l�l���*D�l���?��p��\8;?�up �H�x�񚌦�|}�����wy�3H�4n�����Y��ÂTq�11u�k���_�~x����򁂂�����aeNLY�3PWm����d�r����{���σ�騩s�Bp����#�177g�iI���e��	|����B�c8!G�ë��F��g''�K��H�$,��v�T�qJ**2�,�""���0ؕ�%9з����tD���?'�	����];���5��L���������g�X�$�Χ�#
I巷{G�*��p��CBB�f+�HI�
-t�4b�ɦ(��E�1+jK���5�'w�<����+7"����H�`�ꁵ��篷�}�Ax}���'��p?ߙ��.�Z%�;�U�����p�eW^P<��������:5�y(�aD2 ��ұ~K<����g m��<4�� Ǐ9�]n�>iQ]0o��T�0��S�Ӓ�dLM������Ҫeag��ŕO�s�����4�s��ojr��G�O������'m"��d������?�x]�_a=9��<e�lHLJz��\��?<�>�'GG�V->�ⶠ��Od5����4�����aG���p��������$A���9j��P4Ȍ+`G%��!���Gv�� !��9k�����V�ӗ�VZi�m��os���	؄��k�wy,Ĭݤ��щi����"�l S��SG�+����o����}O=��ץ��D�J똢�Hl�1���J�����-���~�˛";+M�C����(/8�TU/�f������� Z9q Ym��D������rϼA�.L � /���tHɄ�Tb@�Ħ�_�AJ[��ni���W	�
kA$::o�y��ۋ�/�V� *�w��2t��_��)�����c�Pok��.�RE
� 20U_S������	w|���W}��A΋���
6��WV�W����*� 󤰲�#CI�nW��{��02�&�!F9�Q� �''77$8X��b�]'dz�YY�ֲ���LA�>/;;;?��ؽn�J#�M�)����'��߅7��7��q���#�!�r߄�����Ji��򰻘+.���<��Y`�A�5�>)Ϳ��Xͨ8@9*jiQ����n�[���Z&]l��S�Ԕ��p�ؘ?%&6H���J9���;5��噘�1(��m��W
���XHRY���M5��W%��{{��3��Jő�J'�P��!m>���:Nf�_� Yw\d��g@�|���|��f`�
Á��1`�����,VWWO{H��Vi���/4�������keu���@��ښ��� �_���#�J�
�1��և�ǲ%UK��UU/A���k�UR�B��x�����K��	���?%Ó�o��VOX�^�6�^Z� ��MeE���MPP� �)�+�Mt���Ov=���23�R�ԌN����R��kX;"#��9!3�V��)���yV���H���YY"8�<�B�
�gg�]��Y*�B��ɑ����$�e#�ID74pS�h�\�ET����y��1����0��%�z�p =@��-��y�M��V�i;Ɛ����!6�֡_dw
�@�"����Ac��쀀��$�n*����5����$��Ǯ��
�_ԉu�Wo��'�(N�P.���H�t�������=��nmm=?��?�̗��v����wt���SR9]n�
d�K���ֽ}����
�.�>fc��L�)��Z���s�R����)��@ܝ%v��]����獈+Iv����N�V]\�s�����H;l�O>S�&��[Fn� ���p���9ß�
�]� �ΠKӌ�sW��Q9�͚�g�³�*�['� wS5��j���^g[?��oM=餭�R4�����yv������4?�r/̩?]H��[賛y^��b8�
:�3��f�W\N����(��͡�/n��Dxl�$/ILp�Da��g�D�9�
99�,�o�IH߇(�nќ00P\ۭ��	*�o�ƊeL,97���l�5��!Ʌu� ѓ�_ʪ�'�ܜv�;�[#�b5���v�Z�ɛ����&��u���˂>��%e�F���n"���.�wTO��O/6S1DR�˻E����@��OWW�5��t�	����|���-�߇k�$�y����
����j�\FN�і]�.�I���"ʃ�{�lR�Bq�y$ɭf}}ʩ�g`&D}{�����[[���;59w!0�Ye�ann&�`Ѥ�~�L�o�/r5ߡh���3Q�*���̓�#�m�����S���4/��v]�	R�E���������\�ۿ $(tR�Y�#�y�ꙩ�g���"?��r���51)��I��pjWG����87������O z6��9���q�Ј�Jܙ?p�ٿ[l\C�m9%��XN��\b���#��J3&��Z@b�	�|�K�?{���h��Y��L'B@��*�z3L�q%�8�54�R<7�6 �x$�Y��
��^������=Sz�ĕ��r@s��-Jv�2C�jDq�"n����qOtN�P(�N<�������E`'jlF�3w�b@�O^�uk����C��������Z�
�m��@�]]U�nW��ܕ�e|+�c6x�&/U��@i[����$��8>Y�� 
�Oj��n��		��T�4���\k�6��9�֧	�,�p�aL�r98��ڶ��4���P�Q��pJ�ޡ/S�q�i�o��=a,��	��X���v�����9e��c�����3���ق<���lO�kw�L��ʀ�s
��;̔;���W֎.%L�u.�z�Wp8����yq7����+���^�ͧ��(���ꮝ�k�o�"���6���a�(�+�x}I��}�v�����.|�u�p�������]ϱ���[,�܆��QJ����� GKp�� Zo?b�T%��L�o/�n��z@"U��TT�e̾h��_;f�$! n�?�T_�ú���J^����Y/.64Е���EST�X.m�۴ ?[3|�)Fʶ�1,��W�t ���\uD�-�~)??E�U�5�yĝcI���q�����Dt����:��j�C�*8�������Do��I��X+����g�]ݢh��c-3��
���Gi��kw�[-�0ް����ѓ���˼�R]�¯	|��}��(�O�܆L[�F'�q1�^%�ٗ��}��}��| �����K�u�d�����z�&���?<n����򅜺�_�|)KkK7�	R�4TU=^]ٙ�fgGEE]^�y�Y�-&����*�	ꚞ�������Zk����+ܳlڸ����AR:xR��s`�F��)j��keo�IZ��½F��S�S)��;�]�m�8w�K_�/h.�M������KzPqc������^�Y�n���oLK�Sh�m��;5�~��nU|f{ʞ���Qvn;3�~����(6Rq n`��jПZ�R���dg.:V{V�n9SWu7��"9Uk-�^�!{�e{z��Wa�����Zl��|�����͠��N'6��օ�[��_��--�Y99�ܮӭ?�y�u,��o��hZv�é�&��To.i-��J�Kml��i�`V:��j���*}|�cT9�+����:�:�����Z돏��QPZ�R��mџ?�-վ+�
��{b�:$��ko����i����V��"RS}n��(c� =���Hy�ѩV7)v'����������n�5���д��YTQq��Q�A�krv�-��{J��eK��꥙`�'��͈L��(K�a�o�Ќ����6�^���8���8���G8�I�Y�4�ָ2��>[�
:♭��]U\������:^�En���䪍�$��t�}Ƒ�������63������[�ƛ�Ɇ	�d� �)��$��U\A��\�[H=�VH���@��u^���2C�t"��|�s�Z`c	����8�Q�dk-Mpմ�R��l�I*��������-q]���N��G�����)>{��"tYm�ZN)|��r��>'��.۽���:���\|��V�dI&_�~���[�[�
�fXXX �QM�;��V���5�����Z�?GG�[����s�.�0�=���MM%>���x�B��`<��l�t�>!�ev����ӝk�~v�u_3����p��6����,h�ŗ��\�!��\�S�R�Oی�I7;3��\���U���_��h<W��K�0��Ma�V���1�R���Ye�s���
���N�c+��� L��d���m�PP#]'���l ����4�9ޣJIF��J�uu��{��x;Y��<?�S%�o2,����>ht�oU�FM��3����6ȕ�����\r�mj��-��K�hU�I��KЁ�Z@
��Hֆ���tP�GZ
b�R�.��Q�+^�C\2�a�,�S�;��O�k��w�LSɭ'Í�����V���1d ��A��g�ؑi����Ir7d�������T�zE�����+W��)?:��h!a��;
|�W�T�
����[�7
�ǌ+:����?ȠI�l��_G!�7:VW�����L��4��'T%Pl�:���0~���]�)Xa�ڸ;`=��!Nnhx|���k���&'��ҩ��^��cV:y�q(v�p���>g����Qp@u�y}�"i\^я��Wl�����J2�j?=!$&߳
�2i]��ݕ!r�gF6g����BT7�.b�D�<�[a9� ���eYk#;�s<ήR��rt������eVvL/���Ŧ�/���Q_5'i9�-ڍ��ca�%��4V|(°˧Zt����v���!xR�����y�8[�L[A
��O��א��)ڈ�%�����>V>�0�����>m=�ԏ6&��Y����'7��
ͨp���O��xn�X��j�ƭ$"%��ʛ2]�"͑GL$���i���BfR$�!��S8'~Sw��i�$%E�Q	�� �gtlL*�������
��|I��f9�boI(�H(�*��|Nf}�&�m`/uU\FA�KU�3s��O��h��I��"N�!��ފG+�'�$g4!@_�1�?/Ծ���g�|?�o�+*���I��r?E,���<���*ePJ�nk�!�l�*�]ʔ�Q�� �t�Γ�)��j2
�@xA�R��>^ڼC1�ؓ_w����
a4�w�;����/��� 4&�z��S���U5�a�p�0�էw��_5�z��c�&#�㞿�#���,�ڎ=��6��ŉ��.A��E��C~HX���v�cɮ��xqd���!�}�G�.�,�C��{���@��G�c�ٰ'����m�R]\�0~ax)(��.2{�B�N>����P^,�:���u���a�=vUM����8xjح�6�|��K��{y����'�2��`ϡ`�����L|�N&����
}�t�|՟te��	�~��-��D����?��%���6�]2a�d�vԈ��nsS�ť_&�\�KEF��/�9$ ��`�	
p����Goy����
�8V�U��VG$�vq�kp�Ix�޵iyi,�]('��JX��yp`�	�,�br�xC�i���L�7վb�sx��Fɓ��{'��A��{���N�(ox
N7^�Su#R���w*����}!�2�;3�4k�zr-d�����
vWZ�W���^��zFiz�=�I����&�����])��l>y6y5�%���|�<�2��E�~�Ǫ��ZHvvv��� !���,YP�`��?[�&�-������ff^�`��칺��d~��v����PRQioom��}I� �eꡮ�O�2��w�&��t�
�N��T[uJQ�x`��T�_Id���a�\&����6�(Y��\ԟI���ߑ��ı?��Uoe�ֺ.6I%p&�d999�雚�$'*+ȻZ� 7c0��S1�|����V<3�����t  n�U _t���e� ����ı/'��%��jb�zC�d��Y��:�G��7i9]��] ������ �p3�F}�w�+x9�x�P~���/������M&_2�(ޅ�]3�%���4�f���|�h�CAA�9�����O+
��YEE��]aQ|s��¼��"� ��r�#%%���x��D|1$q�q��ߊ�z���d���
����M�z�֎\\9)�-�6d6{��ױӤ�	Zގ,}{{8u�v�g�W�i�����
�NMECE�+د9��=�)Bi�Vwv���$���X��<�h�����		��������o'� Y7�b�����v,���}}�
��>���nק�hh��pA2X��5�>5��Z�Q&@gblllbRLg1[�q<W�OHD�KL�4.H��ע8>&S���
Y�0�����]�u���Q�7݄�z�%���9�_�zH��� ��0�Î<+.GWw � |�{�A��-��RSSCA@�Y�HO����؟[i�B@GG�����gL�����ZX���//on^^l��֙�����И��:���>�9Hi{�@}lv67)����{Yّ{��^�?G�쭯2��L�b�|����#5Wǂl y!	s����9f:���x9&

S����K�.ȟ#Mi99���07�����絼�����ȘULmd��( ��,��9�����k=�-1�]�؀��
�"�Ī��Sl�?/�t�B���<R�����R�R��B�Ba,���ik''&.��(
aC��"��h��&% 8�X��Y�`�^y �'
$P�3��dci��x$����e��:�	��oݪ_��%��FǊ�2R�k:�����r���?VE1��H���(��#����V�'�+[I�%�ߋ�d��阞�z���z@' MxJ������ZyX����Z����������9>>���
M��o� �M�@	��/��������G�@�Yi�H�]��,zs"�� ųR�k�0}��A������d�
A�z�z%�_�<;ȳ�>'x�Ov�%5��"��M�Ě�ȏ{'nb&���p��_Q��%6���)))A@@���:}^EPWIN�0�SX~&]/�ˮ#���:�yn�qJd����t�妥�������P��V��%K� (-��i�	^ȿA1AAA���k&�_�PQ���[o ������=c.��f��)Bx�
��Az�V��-t��HiFb�4�!�V��ͱΊ�C���"[39�DY<�ӊ��˨|%xej8�gckK�g`�7���OTLLǇJFAC/ivV

5�۟3
q%�;�����@L:5
:�����s�+$����X ў�)r3�B(�۷b���<ށw�tk�x�����|("�{��\��h��� `R�������!�|8�U��Ԝ�{��Ρ"4�� ÿW���6��_�ԟu ��[�+�H���Y��L���23���[^Y��B�\GGg�*:���}T������b�300���_���!��?&�A��XEf���Ya��L	ÿ!A�{�<�o�y��}� >n�ׯOn�՝ڬjoz���Xņ��v�#�d置m����{\�H�Tdee��QX
y�r�FCDVc@�c�5$ϵ��i8pZ���I�q3W��x��1�N�@FFF-������x�?�i�7����I�N���I@R�0���6�\<�#Ì��T�Yaaov�B"̣2�������2W���+��t�釧�L0��]�ֲ&�����?5]Bm5��\�D
KrrtGGǮ���ɢ��	E���^��鲹ђm�X$�D��x�Ю!��

BX�z1D�����?>[���4/[��N[Ac�m2��b�����t�<Y�5�v�z̐��^a����p��չ��<�ą� ��\e1�n���St��f��Z��M��G��7e��8/c ��u��
ߝ��nI���� XX`�+$�˱Q�f��,�K����)���� -��@ �:��]�_E���3��`/�����<�,�~��/Jt<�O�#x�
��FW82CRG,�K��J,���B�bY��`�e8;y������UU�g�c5 {,�zZU�����N������/I>��l����nT:�]���� ���솆����6ץ�#�}}@�����/��:34���o`�����h���~�G��/�a����3��>8�6�v*�ے�
3�F�����z��'rdˆ��no�c`aa� Ӯ�=&&F�J�q�Ǐ��E�(.T��E�{q�A�����j���2��-���A����h
T+�7�x)�Tc�}C���z.��(IE����s�b��j������}�9��c����z���Ɛ�>������u�9�/%)��NX�I�+����EI����^��9�ZR������NIu�Z6�F�d��i�;*vl`gƁ�G�9(��?�W,���ӫb���G�K"����濿�: �sp�ߔ2���(U]���?�Q�l�9�f��~iy���n�6������������jf�x#6�`?���ɿ�<�z��q�w�g*��mF�mT�*oE�nnjW�h�<-�%���yS5��fb8�`\��A�&�I�C���P�/$3�?�aÎ�(���o���Gq�CW�^;U���۱��uM�|���0.7�Y�Ԓ2B�V�������]c�d������z��W2���]9�v�K�׏kA�������_ݧQ�͑��`˞9��\r>?ڐ�+��p��.6"�ӈ �˱@M*G$�yÏ�2��f6�U����R�˚~1��蚲��q�19�cu�x,ْyG>�l�aJ`״��f`4��s4��'��A8����n�����x�W�� X
0A{���o4�A��x���9�Ӽrh$j������Cݳ4�e����4��Y_Nfv��;˗��Y���&�o�`�D�do���S�t\s�X�I�&���>������gVZ$9������A6&�e�*YGHx�u��u*�����#�{�Y�ϰ�^.��T�ֵ)�R�˱��� c+lE�H�)�1�ٕ��.�!�u���w��z�\���hRe.���L�6���i�?G��u��D0�P�G�L����)�׏� ���oo�����f��OH��IW��!a*�F�{b�F�A`��S��� �2���q"�����4a�V%K_ȯ��9W�}�<1�#�5�0uD>x�'�v�Enp�=�U�\�t;���%��[=�zm��L
R������u��ZzM[_7n�8A�'1�{�]z����p�����{���
>W�?y��]��Ġq��"�I���u��zh�O�
l�w&%%�������_+���	��Wu$�Q��?x�yzy���'�N�ycS�LB¿(4!��F�'�� �t-7�E�0�@����p��y���� x0�ݗ�hN~q�O�=��jQ)�eL;
��ա<�LL�歭�$������p���qqj?~��*������y2V�d�k󘪪��)B������`�s�Xx,F���@������UNR����!�Q��7�	
�k�����"W�Ϻ�O0\]]�uMM�A;��n�"#rs���є���F g���*��K��
y*�	د��?����A��k򋹊���0�/���E�RRR^�~�3�������<����/ی�YX�@"���4�D�SSW'|��ʳ����}���y���F]]]Ks�sttbbb�r���\��@b��7˺]�}	 #u�����Vv�g�����ϻ�f��~ttdP��^ɨ�Ґ���Ὄ��K�?WNRub��I�/*-x��p��
��j|$6�{	�Ţ,��A��t�%�F0T��6������^?p�?���*�Q����ϔ'0+��w¸�-8��H�������g9W}��c`h�ߍ�f�A���rB�ŧ���̄��u����7^�s|D2�/@#�b'1n�o�������ri_�Xp��Z���6bA
���d���.^\�S0H�ϟ�Zg�ߞH1ʭml48���G~��	��.E
�!�=��K�2�nx��q�k'ks �칮��}0w�y�捜�<�e�e���p�ӷ�����g��x9�'%-E1W)9+K4M����x�l��##րeXYY�z�su
���9
�V*�F(U&� E6���N��-^I��S�W�E�k+ ��H�H�ҝ"�
z����dQ�x�1@J�H�7oh�F�L; 0k<�(HH e(�ol�����������B���IHH��I *j ��$����3�\��CYE%j+�7e�f^�=LL�1��U�����{�J�7^0x|����W:�V���,,����� ��>����4�.Z��Z�x)�l��uQ|bju�L��<M(_M׷oh�*L0e��K�.�?@���PI\^_��������\YY���+׹�Ǽ���/�J���m��6�v�N��t���[��X*�� MM�{p�*��1[�GV�b)��'-��9w
5Y�q�g^C$����r��%B���tߴZsrp4��e'��J�ǥ� ��sO&�o7�%�zt^�lpp��	����y w���(C�������v�%HU���Ǜ���G��i�O����{��U���7�ap�����ť�i�"�� TH`�IL��qZ�_�^��`���(I���{����B!�#���{{�k1�؃�JԻS���	#��?M���n��4^�wsZYZ��S��|��o��v�Urգ�vJ�iOɃYF�44>�O���T�z2�>�M�=n]٥���&�䩗�E
P�Zӎ��j~�����S~��K�OǓ�S�`��2�Fs���w����W�A����@��Đ@���A�o�����f�Z��

|��|/�s����
t�sT.X�_Y�M���{�R�cb�bG��H�r>M�(���L�5��� ���u�NM�9��;�=b�b�xLI�۠��F�&���
>�� ��/��¨�Z��5�����!&�u��)���O������xzL�����U��W�o����"������{\jl5�_�Jlv~j��
<�s�'����x��$��{��f3�;�:��*�Υb�
���������3���\��q��$|�<2ͺyO�ݣ����i���WI��~�l�߾�p�°��g������%�+��D���8`r22�=cc��/�!��sW"�/��G����d�nnuR9�{�l���βYO���(b�a:�@p�M��V��6e���&��T�t�V/y�)00h���O��O��%>\�}�����y�1�F��+S��v���0����}�/ �0?H�*��)()���cb�x���?7D���-r��@��t�ϱ������t��6�_T�;�CUC�$�0���(+��� �|�L�uXb��xꀘ���kV>���#MV����?M�b�.ϰ���AK�
��bz
ӷ����%�8އ*�W�þ��l��Z����|�j�L���JΌ���*��{oF�j��}����k=�x�M��D��oYb�Q�,����g��p?K@��o�1Lk�Q�ˢ�Y�r�-S.�:1Q�U��lB�B��>�I���J�)�� Ȉ1ޯ�ZG������Q(��I"�P��֥�)��
���ܚ,_Y� ޥ���7VR
�L8Tl[:g��ާF���pG��_ͭ!�7�"&(ۉcyFF���$��V�,77~=�	�>��埸w8Ћ(E�a�3��
��,�(v.��TT��7`���X�������}����H�n������|�jfdV���e��V� "G9ul�����}�3FP�sg�
�3�q}o¶С��+<���(��? sh�X���JZ��M�(�ʾ��_ܻ:��j��	�.-�#i�qZibp�]ٚi�c6I똑?>N�FX�:!��H��@b�>i����Y�'W����w��4ù�����*0.�}§���cC7+�����ΗV�ֲ1����ɫ���[d���`�vB��}j
�m8MX lfm��pKP~ĕ]�?�eXR�7��K7�ƣ�
��������ʸS���w1��J;Rh$/�%��rsC	>�h���c�D�A�]H��Nօ��U�(
�W�q�dTS�p[�Bj�{���q��)���/��T�R�-Z��B�����J���,�a�{A)bv�?�e~���䍋Ğ���b���c��N�9����k�
7�ss����ӕ����|Σ����b���ǜ�ށ�Y���U��/�K<k��1�!d�/_����,�	vn���no;ɲg��虾����܂�������а�����hA�i�1/ �bv��Z�SQ�k��Ra?T����$�?�g�}�tEwĤ�K�߻5EL>�� 6��B�os�Ù�g����Ĵ�����q ѹ��J�����?����˫�Z���||''⧁�������x�;£ࢼ�Ŏ�6�	F���lh�\Sa����i�>�o
A���p�d��y#��n�����/x'w�_=wvALz��=_���R�I~��@��ZQX]f8��`b9Ye?UH�eT�W|��K(�ٓ)gg@U����b��{�#Ă��xas!,*a��fz�*�F�3��K�y�����h�(�������a�	�\���8���n�@��* �27�"<-<�h�U�eͶ�)�XuDX8o�a�'W���D�SC����=[C�~P���h���iR52��g�������缴kE�"sC�5�Ls�+$Y��^͏ֵ�����&�є�X�����t-���f�b�x����+�N�ڱ�����1�W8�V��,����P���D�텟�G�v�z���L̿D�$���@s�"��E�ۈt�r:��D>��\��B|��(���L��"�}�6�$G�g$�\�A��%E_v��G�T]��q��
λ�*O���H�FP���{}W1\�"&"�Te�wk>⬚�!�$�wܧ�K^�.��5}!9hBMz�RB��_��sv���12J�bl��@K#jD�_�
��>ߗMҰPD���d�C�����ft~~��G�T*��BePDyc�^*�#ǃ�VcXA�u��2m��}Ҕ����"�#�b���|��I�Bs�J :q�Q?xy��U�qmq���*,l<��t�y=�K3�-V5˭��/M�rq�S���44��U��.�I%�{��e�9�:eW���t�,��@���o��l��g�_��C�&*��S��1�:XKr� �
*/����]������Ԛ^�9��)�ߍN�����>�Y�(�|��uB�)q��	~��p=�W��G�������a�F7ۭ����H�����@��l~_q�v��{t�{f_ok��Ջ��E7�$�^������~O�<.�⟞<��d�c#c���^r;�o�0�0�]p�^�>FA}t}9���1 ���SѣQ]]��f�mm=5�,e�����d`8yٚfƶd�!�
�N�F�u�h�98<bN{`Y=v�i*�͓|�Bŝ�����2.~s*U��Y�� }W�Ьd�Y �Ѩ��k�l�[RR�����D�?�eaa=a�M AvH�%��l�z/�~_O�%e��+:�9���,���#(����#r��C�e	clu���W�]���c������""�F�A'�R3�=����E��Ɯ3��m�-8�m��G�e���Q8Y�?�I~kc�n�v����'^��Փ55#�C��=����Oc�h�!��#Q/�{1|�>���KE��x�ʏdrz�j/��v_��06���v���z�03Eݡ��%-�p��S<'��-Iq%6���T]�^�º�!|�H�!FR=$��P�ۣ���)q���j�fxя�I\jA����3��_E�-1��BzzVV*n����Mʏv�����\IފS�\���O�.kG�-|a�.?o���sy��}N���X�j����0�R.>ej|�Q�W$��x�k�v�<�&@�i�ԉ����6��VG�V$h(;��!��2�����#�@����N�*�;4�.����AF����O��)��g������fE�Pݾ�qJ�C6Rh�]�6HC�9�Ιx��
��h�{QM����w��ĩѝ����P�Ci�v���(ȓ�̌�nVV�	�ͪ�G�v�އ�<�JkX�qPr�_�Qz����.�0���
(�W����~E�q9sj�@"bg��ϖ�#-wBI�K2 R�R�@��!�����n�!H_z�XWX�`�QZ�Zk#}J�3�?|�#Ч��#9�W��v%�� �CӮ�E�gs���HP),inN���sVf���1ʙet�gA�5�Bq���q��\�Y���z�f��0s���Q9˲��η�
�V��'ҁ[vo�j��#��=��~r4��	|Y,9�
5�8D7�=z_�����
"-����R>� q11-Zx�,�����{ʚ4%u?Cc��.UR����]W:+h�{�Q�lN�)X
pq�<�ooo>D|:�K�����*(��///�i���E��R�d㳑nm�B�����y���k�0�~��s24J�-S���]�q��w��W3袄��G�_D �b��E�1��@�$DIn�fʙ�i�:d �}�!F˸�$��u��'>L����mK����1�~vh1���]�L��꾁48�?L��گT�V���� �NC��D�V{��u�C����Hހ�P��8���C�f�W/���%וR����;,_�?����Ě>d�JK�Z&Y��&��q.vj�딋l�c�<D:��(}ǣ
���V-'ǂTxC܅�l�OSLv�H��`nB���g�Q�*�`?��*d3�``��0F*�����{D%�g��p	�/Y����[H��)�⌊�Ꞥ���ĸw!�����#̈́�%��'膨���J���v�J]�1D����a9=�"���i�.9��Ł���s����2�wQ6��	�>�3�0,@���#��� �2���Ao�ak����7��"�T�9��)�>V SW<��"/flp��xm�*�{f�.VƘ��z�tC�f��iT6������/:G^���*�!��e �q�b1�+h �ۊ!A9_r(�Y�0��S�鰕q' %�lZ��+���
�zԌ����;U{����ψ��I�?�W.
�%$%�j���c��w�#\uW��x8/:m�e�Q&]*(�\h����pIJ�` �?�s���3�M�?٪�1��k14�~%���m�&"���n�!�i��;��W�kF����g��Dy�bR86������<5r,�7�N����o3��_����OÆ]��7��<S��F�G:��<��Ī��b)CV�*I��Q������������*��-��g�z16�ʪ�7��ǳ��ơ֢� �,�%�h��"扡�>ח�[�˰8G��&�	��Uջ*۾�1�]`\��&$6�$�" -��w��ٗg��|�ZNȥć�����6
9����M�ϴ�y��_]�YgN���m��ٖ�^�_-"\�E\(�w���_9=)XW����3pA$�>�:ߨ�G�_��f��1�;F����5��`8�4�����~_rQ����q�Y���tb�EH�-�v�O��)�l��z�P���u��*_�2r���yv�u��S~�3���)罶��.�#:L�m�*�gz�.���ՏL��E�c$��� � !�vuOX�}�D����4�+��d����Ǝ�E�6�n��s����N�TY�Qm9�2�$\���m7�f���	��?\����'�xixT�im{!���^m�ې�$cVb\L8�������Y>�8
�Ur�ݒW�L�����6P�g_�r��j�joüJe$8'��%�b6�>�P��溫�2�uVonn�.�ȣ�t�e���/��i�� r/�g�o�x��7&S�C�����I��$E�˚h��޻��41�Q�y�Z��0��5��;|q�;����4�!a�׳���L�2�F��g,�3Wt�E�c,����6���\p�[����b�P\X/��prh��i?�2���.�Л�����сI+%���У*�{�[�S"/!����%c�Gv�CHj�d&�`P_��$~�|�uOZ��r�5�0���lb�/z����w�k`z���r��{^,�~u�	���ϔ��*��#P�6�JQ���oQ��^�3��q���^�`&5�mk���PQ�K�~XoW�擧F�Ģ�t��K/M�YIHG���<�B�oR�,W�(�{
�ԲaMy�'�i�F0
Ǩ2~�E�����H���1%�`�,�x�Ѿ��&דN#�-��'��[R�+7Dыz�W�<%Um #q���m��ߚ��8��v�jv�^�9�$�ٮ��u@+��s4쐤\?'+��&����!ϒ��}�X�o5���O�T�5�G�[�ʎ-�d�LB��O۱�G�3f����ABSg�}ĨGjNF�tKfA��z5�hs��d���We���ʍ��TE������_��Z�p�2֎�eVTv���<�zC�Ɏ�,���c6�v���lL$Ќ���`�ޮ	`>i���E[k�"���$6�����I^p(��ӌfxpյ�2�Zv�+2Y�t��"�3����l���ϣN/�Xn��8Kтx�*I:8��&C�/���%�p
3��W�*%����,��+�P�
�<�
�^���zZ6.�꒢�����]h�N,�'?u�f3�4�i���T�2�F`�<r�y�?��Na2,�TV�8�~�,��[�z����$��m�t1OsՇ���x+l��j��0��כ���}>�JNak����v0����X��O��I�:��P:��e�Uu�R�iKc�$]RGfߖg�5�����]E�cC!#���"����{��^������R.�4�9�LD�P�GoXF�����o�0;"�3��d�+W[~M�e
�F�wM�\u�Y�2	���o}1��"U��Z(_f�2��O��*C!z���c�1�><��^����Y߃;9��l�Uy�Qb䃨�����sL�I��ќ7$�v����7	�2.���J5+SRtf-�ƹ�|~ս�lt��8K���5eY�Oq/���̪~�7:� <o��o���;*�W+�z�g�F4e�*S��_L��� |U�P4
��s��l� |�=����C�v/�Gg�wri�)<��жes�s8��˗�+��N#Ϫ!;N,�)jֿŏ�z���⮬�_�Q(�"���"}!���|<]`ҹ�s��GW{���tx8����1�OZgA�\ިv?��\P1>j�Ǵ-K6uy�a"˩\�%rՓ�׳G�v�_/n�W'R��6������1W��%��v݀]eeC;u�X2�-^�â���A�� �+HJ�7SF�㏼��Je�xr
"�.����	8��lM�#�e�yA�A��.�FeϨD�ZF�]�2��Kz��ɹn�o���)uǂ����_C �q��������'�n�ʴ�H�p�q�\Q6,�O���`cٌ�Ee]�e�X��׾;���C�p,Q���,��\J<6�{��'f�e7��;���FK�b1���ep��
VȖ���<P�V��s�t%�+�����ZJ)e�v��'Pn�^E�x7\��`�!�Qq^�fg����H5:3���)i\�ޔ��)� �AN�/�|��� �ph�R4s6���7������fl���K���3&�]�?�
����'z"���;��9I��W{��%E���5NkD*�]� V_!��H����~<x��.�^(0�h�u�s�P	���`Do�����������&9t&��'}�����f��F���jy�ϟRJ�,{B(����u(�Z^�&=��ͩy�kU���Zr���T�Jl�6����|���\ʼ�ꄤ��c�P8�i�e�w�`� hE��ŰU�ɰF�H���z$�R��)~<�S����Ƽbɚ�p�����P���
�"N�A�yL$�},�z1��U�4�
ߛ�|�N��{ab�]��Y���MMd�f�N��������ʣ��L�źn�Պ<���;�	P9Z;yih�I0���0���~*^^���o�!(喺���~T����!��퇸#M���j���a�|r&�+3+���2�[O B��-�3>���%6'g�J�p�h��h
��Wm�n���GKnn�Q�����ݡ�����*� #^ps�ɫ_�蘼ah�_��q"2�'!��qY��겳!���~����w�~>���kr���ü?[����KQ
CHttu�C���m�z�{y����h�])�T�� =�TLi5؎�;E����[6��㱴�����M�.�hS��i��>�����oϕ���=�!r��JY888�ˢຬ�j���Z�>��v�=�\n�&:)C+;�"��v�_�Wv�-�����!(㢢�d
z�g
��?8�ѳ�;w�����v�+9y!�դ�vہ'����f(�J:�����>(H��n��~�[Ɇ����QI^~��	F4�꠻�Wh�W�>8d^k�b�,N~�%�e��;���?����5:Zn���L[� ���&&���6�a�H2����~��H9K泥�����
*�*7:��I^��!p��J\/�?�BԷz�zԚ��X�g@.m:5g�744���ºad�T�
]�B�"����1��z�4%���L�e�Ub��H�v�����.PC��&�?Ņy��x;>�۽1�֠�.5~���bp�E|�+�Q���kDKE}jnn����&��P�����v0����3|}�L�����0�_��$Gq㬡^������N��V��
q#�+��8 ���������,��b]p��2��@�4�.j#�J�8�.���|��hT��(�������m%��{��`^T�AEQ�3<Bɀ����ԏ�1Ǣe�?�y0�R� 1jη]��a���zޟo:��j �@p�##s��bˇ��hV��/���Η�f��@$��^�+����i-�y �ݬ��R�@XTTPgU�F�N��?a7�/���h$�:�n�r_�i���ëȂ./?khL"jki���,.-��Џ�9> �;�x�v4|/�OCg|��
u�
m���
�K�-���2��w�$@3��ʹR�dy��G[\�qz₉x{�����a|d���!#�C������C�ڵ��tM��J\�~�~,�ż۹��+��z��'C�������D����B�;àOĲ�F�q��z�0� ��ׇC%��o��effG��N<��I��ۓ0����ӥ�RMN�v��/A�O��vg`�qj7��������F�P���ĄI���UF��h>��Kя[8>\I�,�wS�0�֬��6�"wNw'4F���o�O��Zi�9B#r===�q�UB��j���'���!
J�_�Hyc�%$`�V�f�M�>��3��q�t�7��U�����~@&�����!�}�
&Q\T�<�����gg�#9^��j�^�e��/���T:EV,��W����2��z�@�h�~;::"%@C�Y.P$�V���*�+����Dcѵ-mZ�<�<�=��pm@�K=NH?�Z!�-Z�>F��{��Y������z�aH&�&,0љ��*-e����qpu-�pqsw��	��r�Q��`
� Grۯ���M���{Y�!Z�]��~�� u���V|R�T���������M^�9���=�Y K�?`Sj𸵶ԭ'�$��1��բ����xH������b�|��OXMf{a�Y�l�C�m!��
��Sp(!�1�8X��2y�d�8��P8#��)�����yl��b��2u���ӝ>yr�a�8i��Μ�#�~���h��ޠ����&�IM�41��Ǘ-���dm�z�Y&2�#��V��?�8q~��K'�p.�wL�r6gggK��ϡ�AA���!L}��H��z�.�w�.i��BB�ê�Hs�Pq���`e��v{L*��u��=.>^����&׍4�����kPN�@�����V"�J�W�2�2�4�hb3�J�OFL*�Ϳ3 ��$*�wꬆ�ľU�q��my'W���˗���^�Oq`{		��ɱ9=;�z��u������G:�O��Ɔ�p"].�>ȭ
�S걯����������a���h6��lQ��������TV�?m?1>>�ۓb�U��ǆ���x=�����4DD��Zz��{u�#���X:7\�t&�@�P��o_��r��a��ݯv0o�da�O�P��`�(�f)�{E�Q���	QD=sC���ȺHeU�")�J�F���������$5�ѬiN<&�QD�.��s`ޠ�h��Ԇ5H$聥C+,,|�	ˬU�&����;9��кAĳ+ķ,�I���ȶ�٢ُ�.��8=��%���6ʴ���ӹ��u��QG�Qy� ���)j98\F}�<�)D�i�l�s�n�M��2�?��S�S��&䅂�7� ��S��WY�;	���:o~# u�mV��p{(>��>
j<K�2+�%����
B�HhU�����V�d�H&RJ|�p�S6��1��0����XB��@���Mx�Dd����1-`J�듌��������_"�4L�,�c	�����eq������	�5Es7�T5rl�8��u��TK0R�;�Y�
9(rX��7���9�
3��R=L���Y#�U�_�4$�����V>��6#E����y%'�9�p���3�43�qG:Z~�4�
u��� �wX����1|ty�	EHC�ح��-�4�孍�+�*�����ExM?��[j$�&� oH�Awlq8J�x8��,U/]R(")~��pR�D�	 �T.a�C�u�NI��v�l�=ێ?� �C8k��
����L��۬j�i�!��w6/16��|�L ����u�C'��.���/�;:���;�#�`��aw�ʔnY�?�J��r��MX K���u��X��B;�8}ǘ�l"�P�$xz,��Y-�1��E`�����$�<|�̺�;�DO�[���D��%l�i�������V�6��l�Z��O���z���ɴ7z�����Z�}�]�"�ro��
���.�|B'��,E#������M!�PTU_D�mU�&�F>�13�0!|Hw�Z"����=�Pk�^��T��wl�x�R,��G��v���"c��P5�"T�!
a��Qt�+���Z��ga��鎌��ʹF"%S�M�2q�%�%KIJWr?IQj�'��N ͻU�"4Z�H\���q�}��_��.�����bF�Y����PPd7r�/�ܪ��f�X�n�d?|`�ۨ�wh�B1���֮P�L�����P|x2WzpN��ɉ$H��c4�� �/�k��8��#�'{ss"O8f�d�Y���s5��,W���$^��-LB���>��Q:W�2Y<�^(�iM����t�T�߷�f#p(oS���4�j!J�tC����_�u�5��eh��㔽!t!p�4F��T?11��W��(�b����y���،�$(1"�1�%Z.�
�pP�B�)��+��i@3D�n�{�u^X���%������(
�+w�P�H���'hU
�}E�<�[�e)q.~芉m��81��^YZ���3����T7טo�J'� �\��Zt���zx�	f�ߤLN�����$�275�`�����M.0K�bО��L@��q�*�hl��|�Q{��伺�:�#H���D��	@%Jˌ�*�?�h�`�i!-Um��HeWPT$C�U���b�M5���kc��yj���H�� �Ba���O��	C��V �k����NL쟉�d9��ݘk�s����R��$�-��E���s%�.�q�:�}Ξ��r�'$l��1�0���ݎ�V��I��f����������~ ��r�����t���
	���|��"nC1cs���uT�F)�h�.W=�W���W#���2�b�m��v�iT��Dx�'C��t$@�
�ujj��#	^��g�n���]�����?:�������i�U���A,�ש)���!��#�[UQS��11^���	�3lԫ�y����:9؁���+$���&<�9�;�A�K猊�2Ŧ�
d�&n{D$��4.Ne^���ߒZ�H��%9�U���PVG�(w(7k(���|�rr%��7���^^?Uf����g><�����~�c�~�_}�~�=2v^<:������_Z��R%Sh�:d	��C�hT�5;���&���XI�1co@)SB�t�2�����~%��� KҜ�5�G�*��ε�Q^^)�HCE���39DHt:66��Ң�(��Euf�ϸ:��.;���q���,|oS�k{s��;��Ti]�=olMA⏏�Eԑ�U؞i�[����o!>$¬?c�`�<*��Ml]X���zN��A�	���_��8�flrX.��k��z�?��nu�?�x��AB�٥7��y��0'C?I!k--juz&g?�6?>~�-	y��M��L���;Ub��sK6����ż:u�k�j�*����є�{�|���C��]{���G��o<�������A7$� ����G��U�E�K�4N[1��IQ������J����bհV��{�����ʁ���OO��ֱc
1��@# "z<��"!"�/��;H{hS�e�c��|H`�H���Z�߃i�K�����#�������C�A��<���������?d���E�߻����*��$�?V�5=��7�Ϸ�<I���v�U��wtt�ԤN(�4��bff��׿������w�?+�NOO���sX����)��ܝ6_W������~y7)
�w0/�o��Ġ����a���6:\��P�] o{��!������	�7׭}}q�!����p� �L ����3̰���4N̈�4�2�V��nq����+-���_=��ݍ�6I�7���/��Z?��l_Nu�̕4���8r���`��Hs�Ш7��h�����e;�R����H�	���d~���[��W����C>�+�d���Z�¶�7���'�v��I�bI��c,����?�;�s�k3�\��H\��
�.A22ǹ�[�߹dY��
jVs+�22�:yB�7�L��m��xF������������,�v�>��{�M����t�Qo��x	U�

���M@�|n�5��o��~�@�����c���f�/߾�񹞊��o�kVtD�p��" ���A�v|{R���������鑓3�җ6�|5٥&/��%!Đ}'��KՀ�g1����"�卄�x�HO�>��;��x��5,1���ur2��2�{f�v������ �������CW[{
�b���о�
)�d����W?�T��*fO���YA�����
⡡n�-?��Ws8�ypz*��&/Jj�02ئ�tv3��{B�H�[i*�O"�ٺ:�&�?N�g�����6L%L�E.���2�;F���PJF�*!�+��4&w �kas2Fb�����v�����L�)b��y����'��QD�Rt<�8��&W���ɱ��/���V�C��'X�"����ň%���)�
��@^���$I�w�v,�lo_E��ȋHFݖ�J'Jq,���(9@L�]c2Nd

�v�%��`�l�S��9��7a�|�+'��w��F�2�T55��)������=ȳmq�-��اv���R.���`״�y�]MΗ���Rg�'�W�@����i��B��:����:�-�n���A�zs��D����-,<U
Q	Y��"����\���N��8"���>��u�lbbr��h�X�˱��ʥ[
Q5[ss��<�5a�D1�х�K�
�C�fZ�> �W8������SW	Y�|Fa|Y���f7͖�-���0��G���nn�pppW9k�e�\��֛�PH���TP��
��	J���B���Ė"^������?kj^<��ILz~�d�[�(Ug��MI��K$(�P0�k�z�A�r��z."Sa��a�
�)�eϤ=������Q^�,�

7�:��m;w���F�����c�WBn(�:��Yv{M8bvfv��]������ �y�m�l513s{>kw�q*�S71y��H����)�u�O���Vf&o=�8`Y�iԅ��5KJ��+��'��z�
�.�UD�{�l)�5y�������͹X����^1��m������ˀ��J�܊a�ho��r����u��w��{���2�����x<�=���X&�Ĵ�RK�����v��JX�bs�J���a���c�"yYY���a_����#�t6s׭��Ô乀�H��ÍWhU,sz��M�nnns�.g��~�:�\�e��~ ����aq>QZ��yd�@�/��,u�9l�S!X��"���p)5�i�ς�](�"�r�_`�8���}�Ƨ��hQ���>����D+���|:��o�18=-܏"�!º{r�qq����o��v<Gn���_�W!��L����ʄ�U_k�������*����3�mT���=%?O�K���Aw{��QV�V�J���=�]�v��|����,V��f#"��š-�{���<9ʱD���� ^6u٠B&�*��s�U"*/���J��S�m�V��ν0��ʶ�ԬY���b�6�7M��F^Y�r����V��O��3Up�U�
U�X��(�{
�0���_��r{���\&	FʰN���h?1&
�'1��Yx�?���$0������:��,.��5��O���փ>$�D�y��XZ0^+�r�cx�VѨ���F9�a-�4_��;��'���!W1�/T6�Qϳ��pZ-bn�=#>��i��x��y�<))iaaA!�o��n�p�����"����iq:�Z��UD��0��TN�O~��6����'Q������I~�E���`�i��u�,�$~�V1:;;�H!K�űd����
�����~PP���?�HEA�n�~�n�)@Z����U�}P}��+���tc���]�ǫLAa���7�B�J�/����|6��*M���dGJC���'�'	늛�f����x��� z��3��@~u�E|2+k���(�p��Bӝ3@q+)���۾�y-Ө�������Y>��X��uH���o4�ۡ�M
�\Ғ�|.g
��֦bcwT%�Cp�˄_�m�3T�8�Ū�˃oO,�A
�z�~�΀�=��/�� (p�RWB�0%�(���ٹ�����nyy����j�$�jg'��+).ux�����<���c9�I������B3k =$ج������:��a}��e2��������+
�,Ղ�V�"gg������V�Ӡv9� v��B��Le�����T}��m���شDn��u�8���pk�5yi�}{��2���:�)����^���/X"JD�ԧM'���bY���$�[����1]�����J#�?x���]���*Y�6I�%�
��X��`)/���Qm����/m$���=V^~1���0VT���=/g�8m6?��B#�8�ᣂ�T/���L�-�k[=[=Ou�A���u���Wik��U���ex` b�u=�	���-D�k�s%6bw8E����`br�-4�&*���u������a_v�dQ`c�L*��L�@zU�bfffR�髑���E�J�hr���	��M4�<7�7l[껡Sy8ovPOR�$8�9�I� 5�&��:��H����)�x��M�{{{	��Y������avS���+��B��h��fC�Mϗ,��(\߀�����,dI��fQf!�l��~\,�ZmN�)V��yx�\��.��fC�~_ǆU��+ʵ�M�!�u۵�Ȫkj���x�&�m����"
�ib�O8mX�aZp�[�:$B��h�1l� !���?|�x�e�~�P��PH���A7�����p,Ka+*jk��G���Po%M�+S;f������#���9�W�3����Q��0'ټ�������ab"�i����� ��~�9�yz.�ے����miE��&A���j'�I��Hyv&<<��޼[^X�H���![b�i
�=<�B�_�M[p�WF��3���n�>�1$���/��~Dǖ�?��_{E����x�Ҍ�0��x@1��r�8�YY��`@pXL
�PuA�>8\-+r��F�{81���#���r��w�,�����'Y�"IH��Kj�8�;.S}�J���H�XB�hP�d^ǩ�������E������
29S�!�q��k�?������eHb���w����d�����\���i��S��
(���0h;� u��/����P ����~^f����a�P��x}��tL���0�����Td<a�����wnp�&��K4s�Vߟ�K��}?�����n��nӤ��#=LL�k���N�P6UYU5��^VVVP@�W�)�9�)e.����Ͽ��W���
����ðc�g����S"1��m��a52^��̣���dk���p���Q/W�$5<|����z�#�Ǚ�]�&ўo�b��~QQX�7u�X�[�����G,2����O�*�]�l�<qui��n��DHg����!:��?�������7��R
������$����?)�p�X�ƃ��SSY�v.����;UՅ>�^C:�X,Z���PD��Ϸ���6^_0���"����Bh�MӤ�c+��U���-U�m����|>2���>-���~�G���H_.]��e~y��a��drow���9��]V�B+pFMD�@Q���> ����{���O�(�$�"j��h��@N<�_rb�Z�W�)+�����>�����e<e�B���,E�xo���ү�}	�AB⨬*��H���TQ�عQ�|]����:f���%$5�럕[�He�2�?K�����==`���:��W��'ꕱ��m�Hx/�[Pl�K[�bAb��đL�66�j�JU�h�=��z#�5��p��V'�ȟ�C��������z�[iٔ/mj��c�ޫ?��b���Ќ`ǦV��s3ȶ/_or�nл� W���H�����d�n�@�d��?�SA������^�nɱ��\�s��Q	pw���f�;y"�eA2?`9�awg�U�BW]�+��_^���[]�Z��=n���p���@���)���b'�S�n��[�T��|n�U*X�����>�?�&��)E
C�8�����W�iT�x�lvdvSq���m/�*�n�ؘ�{} �S��P�o������z��O�ށ��&��������:�ڐR�UMq��#��Fַ��Nh�a�Ao��[h��_����2��$B�.������O�(���K�X?��N׏�GWi�G�S�.NN�w�w�"�))�m^ښ�XX�[��
�8�s����C�8���q�kфd������z��J@a*���B1�ѕy�(�����ګÛ���7+A���O�����(
H�q����>�.Ep�/��1�wCW���Q��ɼ����]֬I�����I}磵= ��ǡnϋ�A��iI��U��U�����@|:9*����3**��5�K[4�45�{!����ߠH�)H�b�G.���B�O�tol]��äq�ŕ�H�q�Sg�Jʝ���
`-Vм#������&��j�HDD�E����o[��4�s1�a�q�Z�4������7(��^C����r��R]��d9\U��H�A0��c���zff~�t|OLL���jl�;��'`A뭗���XV ^����R�+*&������UAG?J򖛛����M�]WMw�^�i:"�
���Q_"�V��c�D���8.�)��B9�\Z�t�����u��D&�B�������|�ĝ���A��O�ƶ�a����G��%��c�'�>o�32����7;}�s��'&&��w�K�F��-=�|Y��n�����]���FN���t�k��Yx���.4��(a�im7��Bl�J�n0��E�?���ihF��7:<\��:E�~ٹ����N�a�S��e0���u�\��PȂ��j=� _N�s�����>�`������L��t��Kf��l���+�.�5���sD�V��tƿ�j���F Ey����KiH6��창l�X�3E��{*T��a�!�58�h��&K��Q��a������9���od�슂�Z�ID㱵�D�ŋ�}����$fT�d0_2�SտZ�-'���Yت�S�}�1��ŧ�����Ă�j�
�z@'�kՙ��8�"��jPPAiN��*�K�b̑��W�����D��	QU D((hK~���B�o��0^C��1���� ���X����ցC#�F�W��w�
��4\��L��j��9�|�`�v#��4�|�[��/����、�`��x�k���_�G��rH�Ȁ۬\����:{����F%n��k+l�A/��*���|^!�#�W7Z]��"������C�&�Bo�U��@�3;{k�7d<����c+;6�����"1}G낻x�,�i���9kd9d�n���=�ؔI�K��^8L.����C1|"��������ڗ㕇�I�~����nÜه�x>W��X����X@Q���:o�p.vxy�"��"`8i ڮ���M�32VF����sp��L��v! R��bb�%]�#��b�����%�֖e��l��E`���y�+�"���˿Y�m�y�M����7�0��#(+ԥ�Z�x�Jà��_��ThT��@#�aqۓ��\$�����F�J����P:�fk��|>\����h�TM����n�ӧS����D����p����f�wa��8�������v���ylL̃ʋ:i�<3��8�l^n��P �+uZ�j��b��w]��~s8�H&�}.��!��8ˋ�Vx2w�t�8N&�����蒂hH��~�s�l�����u��"`�ye��+Xw�X�B&<��{�z�p��jafff��kՇ; �n2�k����K3����Π���͡R� ��@�B��j
��sqa�3���l-	������#���6���E���u�R(����M�럗G�
Vm���
��W_Xv
���IqWjr�_4H�iޑ4�ꉊ3�Ir���D�qd!!�)2��[��*܄�~B��|����}Y���>��g��{�҆{b��)�%?���"mI�Ε0�*���:�'O�ϡ�,X����*��d�Z'g����q�uT.\�TTT�R����
�G)�8 *�C��
/�����wr�f<� �lx.�?�U��Dz�ހ��뜕�6��J�Q�^�z�=��N�ͮ��`�\z�� �uwR68XgK��k�߬u� _�r}��L����l�X
�}�(���e�hɛ�x�Afm a=���M�/5�<�*��p��!jqE���0/����'�����<��rOO�hq<��q9[�f6�����"0Og3C!$D�ry�;�q:�0eAEE?�
*�-ȷ��H��T\%��7�B ��6�rk���HCy V�*||~��z�D(�b�?�UMjBUL�􋬢)<M��טN�����
�f#]
�K�oa�S�W[��( qGݮ�o�f'&�pqӇ�$����w>J�M��ǖ16�	�&. G+?�R�==<�~�t���h��78���uΓ�
�.S#�r����s
�a����=�	WWW=�n� *�����T��)��Y�P�s�+s�`Dhh6T�c�%���<GS~XTT��V��.�ܟ)_P��
#�<� (�$Z��ퟰ<�}�3p�g&�T�M�XM�u*������r���}ڼk��ivZ��%�W!�b?x[��m�aw�䝩�odc0a"B	���M;�$�t��#���C��s'��� ԮJo���'p<�ޅ�����C �I��M�	�\/�< ��	��Z�y�XЋ�`xl{@� �


^P���c�i��	�������{,&�r�>d9Ģ.1�Q�5�G�}���
N�cIo�ނ�L�<�:7����m�ԧ>jVٲ5,ݱ�*�;i��=)a�����sg�EU��"R�JiZ�H:	%����GЊ�|�gȃ��]Y�*b����S���z�ԟ
C�h,��Au�Н��7�=�����4gWx��:�X+�U�7�ƚ��)��_D�m�Q3ɳ��츻�vv�.�.�pR?�Ω���n��'�<�	燇Z�a}���2�0��
Fa�뼙E�lߤ���l�(��3�%[bnj��{DᅩB30�NA����Akk������%G悸H~���Xq�}�ߞ9�mЊ�����������7��q����pj>�eX�i�x`�t���%j�m�}�ۘ�l������y��7��%!	M��\��q���Mq�{e����\ICaۥ�p���2�H������@9ގ6��f�H�����
�}���P�2�����
�8���q�2�cl�~(ߝ9��d�e+oE���|!��>�s$���vջ{Rs�y���F"�=_�l/K�-�/b��}�e�����BAm�����bJW��b��K�+��nGh;#'j��2�̎6���fת����U��r��U���d^�;�/O�͵�
�[u_�-��Np��?�qi�6�%�\K� C<bfl���r���Hx����"�� r�,�w���\�荰��m>�tO��l4O7���W��i;����ى:Tv����(�(WZ�g��:��#��SH�З���b�2��I�Ej�Y�F���`sXs��_#E�������K���C�(J�9)��Y���nX(�L(w�az�%�]Q���l���͗�"��!�����g-ٍn�~N����ҠYΊݽ\�d���H?uv��A��l��=i�2�<Y?��0��D#B6Q��@�\�`n�!�X��G�)vfɓy����m���$���������JPo>Wԫ��"!��y3����5��"���:;�6�� Zp/%���z�4�k��2ȖN���v#�����W4W2ډ�e��@o͈�	V�L3(!vWߎ�X���~���K,�����������#�o�+��,ť��8`r�wW\��"��	�h(��p��
�	d)"���<�V+ń�dL��E�{ʠ|H�Өp�i���
DS1�B&�OhM]]����D��k�[��NK�|
�����S*ȧ0	{��$ѥ�8�ԍ���}0���b
T�L�H\���n���}Y����^	��D7��o�]^\_k�#�e��s�8���Y�T�_ R�k���z��\.�=�t{(�T���
��p�ӕz�����@Ζ�ԅ��'Qh`��f�(er�PR��(/�n�!V斫M%t%��jmm���ǜ*�4��ZXPw;��=C�k�9m: F���v{<OJB��h�	@��������F��d�~��>�� O t��Ѩ�d���oi��dS}e�6����nH�^>��ЁwqF��|]Ұ+9�F�YA?}��,�5�m�to�+o�Õ(�52hp4S �B]50��'���ج�B�`S���.1���PJ ��^O
J���W�+��>o������$q�61x���'ԲX,�z?��#�5��M�ϣ�Tádxᰔk7V�߽�;*�<�����;��E�G��D�v�F���8EY��b�<`E*��������U��#��k^S�:�Ǜ��'W��n��ᘢ�J
�kJP��¥��<_��[�f�� �c�{�F�r-A�H��\3��FS�G�@˩�)̋�m_��y�Tch`���N��i�قȐ��dh|<*?Y���2�b��PP!bOa[ÿ���:�
N���VLjI]�f�J�����_��sYR9�K��ALp�p�'s�'�t��?o8��*�i����}B���Mb�)}Q,�χfy$�Y�,��{#��ЈUe�V��P����̗�)���V���m*�dpⰃ�6<9	]U�h�BV�ߞi�:l�V�? ����n�������������:��
M�`� ���6h��B�迻�������+��j+|q��W�m�
�1P^wԀ�,n�䞾�#��\�&Q�~>���b2���bc�iii ���O`o�&wV1@(���:�m�����(�4�� �w\���Fb���.����{������+E�����['t%D�P�������#k�oW^^�?�?:�AY>���|�$$�v]p�k��33)(+�ù|^��c@�g�i�#��b ��S��������k�\�7��ܡ��p���j0@��=,��Q�0��ױ>hG�JbKp�~�|{��X
� `b���L
�	��} ��כ���m
p���񴷷�a�t��p�|��P�盝�9lZ�M���3�&�5����Gq�I'؇����l��;�2bT��Gr�*m���M5|Z]�3S9�8�Kc��	1�	$#"!���m�X����y�3Zf�Q����3�� K-��2�=\�L�v��(�����"7��;�B��Om_Vշ�Mw+%�RA�� ���F��M+J#�)% � !���n�����s��;_��z���Zs�1��s�w�>������Ñ���p��fp�䏣:��X>��yj� ���˓'��_�̀����]�&��x�K�֫	۔�+��ҽ�"e�twQ~��$�w�/<�Эي�tHr���3�;�!�Y�5�p��C"8<<Tyg��{� 8Mv�	�������
;'�5jn�,%^�&���,�;�*w^}�/Ƀ��Z��x�s�C]i3�'kE�#��q��~o<
��w�����O�� 8�D�������vx�@N.|��XY[�0�����Z���<^�Է�$46��hd�>~�t���^^v��ba�_�7l�u��yR�����H����s�X&F�Њ����XQ��X��ʤ�%.��oM��X�ʘ@�W�q�\���D=�A?��B�0�0.��,�h� ����`3MU�3�m���į�A7��j��dcO�H�w�s[��&�r�~�+�Z^?I�T��[�b��(i�X��Ai;Bϒٗ�׊V槈��5��l�|�R�0E���
ӝ'$jjnq�x쯗}@oz՟z�q�_��AJ���}=J��r�z�}���ŽVX1�7!�p�$��V����@����6����
1���s��S��R���s��<ё�	�K�<+�P'w�/���������<w���HX�8W��I���va�α�1[��c�a�Yҝ������z����`�=���7�`m�:�lh�����. 4�;[gt��y_Wu�V���z��Q��t�屠�LZ�%@=ZW�S���β)�d�2l��)r�a������ȗ#m؃l#�\��r����V�iYe<E����i��Ub��E���t�z��L|+���x*�㻂��F� ��z��������ɞ�<�l�|u�Id4�m�� �蒂��z^�t��X��X���=��\X(�g}ے�c�@��p��A�JH����4bi�$�B�<�	��ou��>?rfT28�?����ݧb��O�Tx�g!�N�;�����T;��M\v����Rn��z�Q�۫%W�xLmLj.���%k����ɓ�ݺ��C&&&!��x�+���P������U��B��?�ө�h��6��+��w����
�K�g��Y�������ɶ�j�]~�����^�M���Mz3��4��nzZU�����	��F�a~_�V�&-f�&�j�)C�ޝ�oH�����o�m�������@VVE2E��>�˭{�;1��	,+�%��<PO����tLu�K�����u�Ÿe�(h����Aۖ�
�U�s�"s`�#	gD���yE�6�Jе����x'��x�Ae�e�d����zƳ��ٗ���ף�R6���2�li���EEUrr̹|�1199�/r�_�k_�۠ x�-�l�?�V����1��l���'bm6���Ȓ���t�����uo����Q���k�{5G����ĝD��LI1�^\W���҂�0'��>��N��jI�^�[���_}��@t4R�$>�Ȁ�����5��ª�s�"����E@�Y�b��m*�a4��2���f̂<B2�]TN��BK��.�������jq����^c�"|�ꪥz�������-�H��2�v6�N���æ��흡��u2�TW\s�=;��-jDT�$�:����N=�ZvS����e�Ow)�*�Lj�vN_��٧����2�"��P�c��;�=/���|��.��ޞ�f��bD�y��#8��
	����7����|�3���.?�}ף����s�6vF*<�;�"�$�N`,�z;�D;�;�˞çe��[Q����W���'\C�Udtr�
Ey���,�!!2Z�e��
�1U^�/ݧaڐ�!����ďi��y��iW�Ջpɯe�t���������`�y��t���v+��i� >�]�������⶝������V]c={�
GBA	:}_oH�+�p�IP.�1x�����B��
�mF�Γl�h�7})�L�E�0��Z�����d'���6G�'K��r�hR.�ޯ%7�6�Wֻv�z땈�ٮ ��1$$&*>A�x�0@��@H��Ɍ �k�%c$�et&x���ѱ�����א�O�@v�-��M_�3�&�H�;�������_�%�⮅o�@A�S�>|_Ѭ'�~�����M�n�5H��_][������\x�Ǫ�����w�I��G'�M �(�
��˖)5���������`fTj��it�ɷ+�Gt�#�5�A	��N]��2ۛ�l�_K�+�e*�ȭL�V
Y�6�"Z�Д
���R.�(~4�c�;�)te��0n#h�&�9���6Y$�D�rj��j��O�vs�&��Ph�$��4�1 �)��*)����׫JW�:.k99��=�]ǎ��<�[-X��/�ͅg��A����S��^0P��3�buDg�W)u��ɾ��c�]������w;�}�����*I�A����v~��ȗ��� ���	�RY����kmfl��"�~6�y�Q��NL���ee����I/ߢ�j*���қQ���dN�I���,ڍt�`(2|��S�Y�`}���1��ɚ<E��V�ɾ&�����3
p����:E���Ȉk#nam�E�lT:Q�N+�%��+��Fŭpt�W�����d
�v�5�vL�Ɨ���Ƙ�}�j�jD�v�\�jyX� ��^�{sJJ6��
�er*0��l�+{�������?W����c�313��ly�Or�o�	�
�e����K�:��	�_�?k�5�_��tUE�6
p����V�+�j�p�WR��њv?�m�NU�J91G���bsY� �*˷����t���HB[���Cdd:��D���j4{���z��Vs�Jߠ.P
�01F.#�U3�רY���n���8*�Űj�}�qcZ�?�F	��[�}���
T�g)8<��Gz��q@b��+
Oߴ�����j�^zC�"|�|����
>t�5�k{�j;:�q����}ǒeܾG���5xXӢby.���Q�kv��]���t�Ꟑ*ڢ}����1(�a#6�|5ւ%R�K��U.�����EP��O�e���'`z��ظ��45�n�j���Zw=*c�ӫ�;��l���v����[�AEG{�+��9I?�t�G���[�{O
�1�es����&~cGu��ᆏ�[f�J���<�7��e��lpl�;~�t�}U��� ��%��FP�>/j�z��p���Q�=+�DM	�1��(80�
Ot�j�*S�-6�����=&�Y��Wɢ��(Ej�;
�c�X��_�����k
��,*�
��'�
&:�#�a�$?���0�Z5Hj䌶�վ��$��w\Go�T����*7�r��]�m������歎N�;�<0��/�?�LJ�v9^w�
�X�.h���;iF�#��=n�J���bg�㞙k/�ֈ�w���2)�i�'w(�����^
���`��B��r��Yvy�]d���]!��\)�5Z��oХ\僅���:�M����5j�ew�99ʄKd|U��'��-�x	h �
�eR
&nR����o55���&<��2Ф
����	����S�����������A�ImO��r���F�̬�oڨ��؎]?���i{;�$��/IU��!��/���4w)cԗa��a��,ԕR����vr3��G{�*��N
�,��!�3�=�<����ɭHmw�NztCI�ugpz�ڧ󻇮�=�PK/�j$\  2  PK  0K               docProps/app.xml��Mo�0���Uŕ�-��
�ʸ��,r�I]d���������3x��$Ay��i���A�P�M������Zkv��}V�	<�+�FP��Z)0��Ҟ��K �"
J��gcgԇՐ
�,m,5�#W���k-
0�3��r�����bl�jf"���\7�f�A]b��'+'�\�,+�e�3�舎�-�VWޠ�*�AZ��(��Z�6gx�DY{PpV:��zo�(l�6j'^������q�G
         @ 8  @                                 �	     �	                   �"	     �")     �")     �+      @�                    H'	     H')     H')     �      �                   �      �      �      $       $              P�td   �     �     �     <      <             Q�td                                                  R�td   �"	     �")     �")     8
 �
 B�( 8X� � ��%@ "    $ 	� A @0(��� B �C�H	�D�!p+@F����4�`1$A "�� �PK�� 8@ ��R� 
�G"��*�a������   �p
m�(�*�
��S0 `" �T ��� 05Љ �  � � ��!P P � E�I��� �K, #@ `4B  (�� $���� �		 P QAЈh @P   
�i@B�H@�  � % �@(    AHDH-����(4� �� l�!r2���F�`��� Q,    ���  �@    �-�� �@2(	TL2C�)@B� 	���I� �a!rH  @@ $ � �H\J( ��[   �B �      !A
0$P8, t
��D  ) ��5	K �$ (0� B*�T-� 
*�T/*H 1��B(��8B5� A	!�  �'�`B�� A�h 0
P P�HA  �@t)B��"*= �H �PV @  � 	�B � ( $ BA P p @ A*��   ��P�� �ID@�Ch    (�   P 
Y"��! �� A`��2� ��@(L 1!� Dh  ȠJD� P( #@�' � � @d@ �;��� ���A�B$�j P BP@��b@�@�!���� J  Ԡ�   @� � �����!  ' D ! �$D"�B�� @$ @@ ���LI�5N��@P� r#A�@� N�@dRD F��� %   �P(	�� ��4�@0� l�    \  ]  _      `  a  b  c  e          f  h  i  j  k      m  o          p  q  u          v  w  z  |  }  �  �  �      �          �      �  �  �      �              �  �  �  �  �                          �                      �      �  �          �      �  �      �      �  �  �  �      �          �  �  �  �      �      �      �      �          �          �  �  �  �  �  �      �  �  �  �          �  �  �  �  �  �  �      �      �  �  �  �      �  �      �  �  �  �                  �  �      �  �  �          �  �          �          �  �  �  �      �  �                         
          
        
                                            !          #  $  '  (          )  .  0  2  3  4              5  6      9          :  ;          <      =  >      @  A          D          G  I  L      M  N  O  P      S          U          X  Y  Z          [  \              ]          ^  `  b                                          c  d  e  g                  i      k      m      n  p      q  r  s  t      v      w          x      y  {  }  ~            �  �  �  �  �  �  �      �  �  �      �      �      �  �  �                          �          �          �      �  �      �  �          �  �  �                      �  �  �      �  �  �          �  �  �  �                      �      �  �  �      �          �          �              �      �  �      �  �  �          �  �  �  �              �  �      �  �  �      �  �      �      �  �  �  �  �  �  �                      �  �      �      �          �          �      �      �              �  �          �  �  �  �      �                                       	  
                                            !  "  #                  $  %  (          *  +  -  .  2  3  6  7  8      9  ;  <          >          ?  C  F  G      H          I  J  K  L  N  O  Q  T  Y  Z  ]  a  c  e  f      i              �}��X���Yr�Ћ�Ab�7�@��?x��(��N�?Sl�,ʦ�]�1C;~C�ïoY�-�d|6�B��g�ɑu��x�-��p��𮓌��	ľ�ag��揗�,3�R��&g�/>V����j3	��UA1���o��H����GM�v����_���у���@�$��Љ+��Ճx�=�MM���T�=�0�p�Kx6����cy�Z�<=�Y"I����-8ي�K$}
v����c��V�CR�?�j��۹�+��3�:�L�����o�F݇��}�b���\������oY7[�4�� gY��#�����jǫ�?Q#�|��2�+Zh S��o}� �IL3N�$<����T�q'�R�+������Xr��`�h*�BE��t�b����MM�������T���c*��S�|��ez>���F�j@5
̍5�AR����Yr��3��c�
ŀ^�׽Q���`���-ʘ?E?N�}��񓍠$IG�S�6�1<!{F��H8G'�&'��'hU.�f�ͥ����7V�=�[�ǖy�yK�q=�2}&36'�@N{u�`(��20+�sV�^�Xp��B����TNq;@�ۙd
�`�"���7�G޷�$+i*��[cVl�飾K�rs�p�#��c�2�����pg�G��r��U;d2��&�{,��r6!��IKHB ]#o������o�v����ϛw�+�X;3����\
��+�В_o�e��l�T�c`I1����I6t�� #t��
�b̨_Ӵqdm�ͫ�U�.yWa9��P��#�e��c���Ճ�Ib�_�rQ!�ϻ��f)t)�k1�1�;��xBY$�����>�1���D��c�Z�d`˿�y��K�`d��|i��u�"/ؒ7'%��r���i+����)M�`D�3�-�� 2�Q��!Yr���S���2�b���~߯g��li*�G�{,ٖr�p
�5,�Э-5�̨�#F���/��@q�F�aFL�e�{1�=��e�4��?/�Xr�@u`�b��>�@o`��l��.'}#��DFo�`%}k�b78ۥ���c?}C�mUqy����`��f�����"/;��ի�Y<�G�-X�1üF���`���=�#c*�3?ov��v]�C�����O����~ߔ�J)Q|�֥w��VA=ߑ`��C�GW��B���'#p�_<Kڙ!�Xi��FXho���F�;E�`��uD];3/
E&��>����3���	�-Yr��|�KG��Uf������5�>98��Wr4����5X�GG�����[�N|}=�tӜ�x1"?�k�X�y�<o�b��-���
k��F�M�c0F���ˢj3:��E��;��P��ɬ-�'ˀ��\�zT� �N߁=i�1�`I{Մؙ��C뒌�$2�+�������ygh&`J])8݀���h�T.pM�1LGoIiط��Y�s���Ji�)�.倏�o5�}D��B���]����u9PG�oxFuC��6+ס�f������Uzq���� Y��,
c�t(�+(�� =��5�����c����J����s��?+�W�����P����w��6��X��E��g�a�te�z]��紐I *ƫ:�n#�lsWr�M*���L㌳�$ꥀ�X`)�gSc5XW��?�)e�� ��ey�G���﬇!����"/�-Y�Yl{����	����(�S�z���6�ʺ�:w��c�  �A�e�1b=+�]�ϻTG����H�V��^��X-���������`��o�(޼φ�cE�,m�/�RJF�y��#��9J��QSQ����~�Ѱ�'�2�c�4��=��Z"q�����%���g��\�-�g���5d�5�� Zcl�MP����K�Yk���*E��wF���=�
߫&��2���<J�q�͜(����Ƹ�mdC}�
���W�pǗ3�e;�Z��ǰ���"�9���%�c�;�`�sG��Ѱ���ܥ/��QEV�
	�̪�n�����	��b+إu;bk��2+n���
�������R�/g���AX��@����p�#`7�O���9��B�i;�k��Ynj��{�`H�U����y9�+w�*��η�Hͪ�S��4n�Eވ�O2��Y�%�Q�g5�}���75/I�̪<�+���z#�
}t�_�Ɓ
C?3��%�O�}�bdp^�����F���
�[�-������+6�)5��Ȓ���4�8��c��}[����x���u�y����q ��3���D��1�@gCE}k�̪�-�J��F�4��=yY�-��l���|1OVJ��Q��IG���p�\�_ρ<L��ͪ�~��3z��=�:�1��x>�=m.��[��M�F��'K«ľ��q�6$��,者'����3G�y���o��?#ZD���|Y�����%J��m��F����A{�u��̪�5/�ˑs|�^�]��"�T��uT��F��#׷�aM�!�2��j g���fu��%m�?�J`��:Eb�e��MG�H��S;��/n�Mtm˗?ڗ�4�.}�:�]����]6}3䩠��l���+��w�g�\v�$F3�p����g��o�˖�4�_�o���-��y'#�y�"���șK7ĕ��Ga�o�ͪ���a�lM<F�+>3@'yz��a�-��ivz	��y1�+GeM����&�]��ޞ��g�����H&RG��t5��P'z-��;����x�`;�qݸ1���F�`��?��]tK��6(�R�}���X��PQ����I�?(}^�a��s�6��A|��Q��Q����ú�v�+���!n��#J���_XYrI>���W^ί�&�=���� ʘW�����a<G���o�^�a߂?sM�+R͢�G8���G�;8`�Y��$���o��27}*'}a��O
r                     �l                     ]X                     ��                     �{                     J�                     �                     �                     zQ                     �n                     Ό                     �h                     g                     tl                     �i                     �l                     "�                     �v                     o�                     |}                     �l                     �                     hs                     a�                     Y�                     Y�                     �                     \                     �u                     {�                     �p                     ��                     f                     0                     eB                     S                     �                     �[                     rZ                     R"                     S�                     Qg                     ˉ                                            �                     6                     5\                     #c                     �Z                     m                     �i                     G                     >h                     �~                     u#                     7~                     �e                     u~                     �                     ��                     na                      �                     �u                     �]                     ��                     �u                     �|                     o                     �i                     Ot                     �a                     �l                     �o                     �l                     �d                     �a                     Lw                     ��                     U�                     �w                     �2                     �d                     �`                     �~                     �4                     z�                     $                     �n                     }f                     �2                     �f                     �b                     ��                     ΋                     ��                     �                     eo                     �                     ��                     j�                     �                     �]                     �m                     *                     �X                     w�                     Ee                     u                     >r                     �h                     ]]                     �{                     �                     k                     V�                     �o                     9                     !                     �[                     /y                     �Z                     �Y                     ��                                          �                     �{                     6R                     �p                     D�                     Jc                     qY                     H�                     �                     Mu                     �j                     �2                     �`                     'x                     �c                     8]                     �v                     �W                     �Z                     �[                     A`                     /Y                     7�                     Wk                     �z                     ��                     S                     �t                      �                     �o                     _                     �o                     �#                     �`                     c_                     #Y                     �g                     b                     {B                     �{                     �-                     �2                     �)                     a"                     
                     �z                     dv                     Ί                     �Y                     �                     <�                     �2                     @                     H�                     �t                     qt                     �r                     !�                     ri                     Gg                     p                     �W                     �V                     ��                     ��                     �u                     og                     �f                     [l                     J�                     �X                     Z                     Bl                     ��                     a                       1                     u�                     YB                     sp                     ]y                     �                     ��                     �N                     �m                     2{                     Rb                     �y                     �s                     4k                     Yq                     �W                     l                     ��                     n                     �_                     �                     ��                     .t                     >s                     �                     |Y                     �,                     �Y                     N�                     !                     zj                     �n                     B                                          0r                     G:                     �                     i                     a                     [}                     �V                     �g                     rz                     z                     :}                     �s                     �c                     �3                     6�                     W$                     �s                     �}                     �                     L�                     ��                     x�                     PY                     �x                     �{                     �                     %l                     �V                     8                       �e                     �                     �v                     �q                     �[                     ,�                     �Q                     f                     �\                     ��                     b                     ]                     \                     �-                     y                     le                     l                     L                     �e                     �                     �                     �R                     �w                      t                     �@                     <V                     �                     ��                     �)                     �                     �\                     [                     zk                     '�                     �<                     �a                     �y                     �a                     ^V                     R   "                   s                     �v                     `                     �|                     }�                     5u                     U                     �y                     �w                     UW                     Z|                     �&                     �                     �                     Oh                     ��                     h�                     �;                     �f                     )                     m,                     �x                     �N                     j                     O    @f     M       �    0O)            ,�    �H)            �    �J)            �    0�     2      H=    У     :       �    �G)            a    X@)            �4  "  ��     v       O    �G)            �r    �D)            }    xK)            �     B)            �X    p>)            V=    �     g       �    ��     5      P  "  ��     �       �    P�)            �;    `=)            Zt    E)            ;y    `F)            h:    PT)     0        ]    �?)            �H    `     6       �    `O)            �[    (?)                �           �  "  P�     �      ��    �K)            \!    ��            3$    p     1
    �2     v       �.  "  �     �      i;    p�     R       $    �>)            &b    �@)            L_    �?)            �    0�     T       5  !  �$)            QV    @U)      @      �     ��     	      V    H=)            �k    �B)            �x    PF)            
      ڊ    �I)            �H    `     �      �/    Ї     �/      ~[    ?)            cl    (C)            �Y    �>)            mU    ��     i      #    Y     v       �    �I)            B<     �            �B  "  ��     I      �C  "  ��     B       �    PO)            Qo    �C)            �z    �F)            �j    �B)            L9    �t     x       =!    ��            Ǎ    �J)            �     ��     	      �H          @       9	    �.     z       �G  "  ��     D       ')  "  P�     A      ^8     R)            e3    �     r      �    @�     �       �}    �G)            �    E)                pK)            �    �L)            $^    �?)            `    �J)            �    0�     T       �Z    �>)            �p    PD)            E    @�     �	      �,  "  P�     E      3T  "  @)     �      �    J)            _    �?)            $<    �            �A    @<)            �T    �     n      �l    PC)            ֛    ��)            h�    �H)            ��    `=     c       ��    �I)            '    �/     �      o>    @�     (       �5    �1     �      j    �B)            �  "  `E            �
       �d    8A)            �]    �?)            �"                 �n    �C)            Z    �=)            
    �F)            (n    �C)            ��    �M)            v�    �I)            0    ��     H      R>    @�     (         !  �&)     `       �    HC)            �     �      m      �]    �?)            �z    �F)            �u    �E)            �    p<            �    �G)            Y    �:)            r  ! 
       �    H)            0P  "  0     (      �    �C)            �q    xD)            42  "  ��            ��    pL)            G    �A)            �>    ��     O      �v    �E)            &  !  �&)     h       q    �M)            OC  "  ��     �      p_     @)            �;    ��     +       ֔     L)            �    �L)            D  "  ��     �       ?�    K)            "     �     �       B\    P?)             �    �I)            �;    `�     �      �    ��     �      �^    �?)            S  "        "      S�    hN)             &_    �?)            �Z    �>)            %}    XG)            �i    �B)            /I  "  �     ;      �#    �	     {      �X    `>)            �\    h?)            �'    P5     R      Y    �M)            �    �D)            �)  "  ��     �      �
  "  �G     �      �5    �-     �      |r    �D)            �9    p�     �      A&    �-     �      8�    �I)            P�    �I)            �    (B)            �:    �<)            �    hI)            �    ��            �s    �D)            �<    Е     2      8�    PH)            Ab    �@)            �0  !  �%)            �=    0�     �       hm    xC)            �U    �=)            yV    �=)            ��    �9     L       �j    �B)            ŏ    0K)            �	    �=)            �`    H@)            �o     D)            �>    �     �       �H  "  ��           �y    �F)            �    �K)            $C  "  ��     �      �    (L)            .7    �8     �       #9    �t     1           HE)            ʕ    HL)            2�    �H)            [     �     W       Jr    �D)            f    ��            Ȁ    (U)            �]    �?)            �    �E)            �n    �C)            �    ��            }    PG)            ��    �L)            =    `<)              "  ��            h�    �H)            �     �           �E    ��           }x    (F)            [z    �F)            t    XK)            =    �     v       �p    HD)            ��    `H)            �    @I)            �W     >)            �    �A     �      �N    �<)            /
     �       �    p-     �      �^    �?)            �h    8B)            f  "  ��            @�    PJ)            �D  "  0�     �      y    @O)            �C  "  0�     M       7    �7     R      �<    p=)            +    �G)            o     E)            �  ! 
�    HJ)            �    @F)            �0  !  �%)            �<    �<)            vn    �C)            �1  "  ��            �    �J)            �E    ��     �      ܈    �I)            +    XC)            cQ    P�     �      ��    (K)            ck    �B)            i    �O)            k/    �q     =      4"    ��     �          �G)            7    ��     5      Nv    �E)            1  !  �%)            V     K)            VQ    `�     �      |8    �Q)            ��    �H)            I  "  �     ;      *a    `@)            `     @)            .    $     �      ;    �|     �       ~
  "  �E     �      Pn    �C)            �    `�)            W
c    �@)            �m    �C)            Ns    �D)            ~    P,     �       ��    p�)            :t    E)            �    H�)            xs    �D)            Te    `A)            2V    �=)            d5    �'           �    �S     ]      p    (D)            �  "   E            >f    �A)            �  "  0�     l       \�    �M)            �    @J)                 ?)            �a    �@)            c  "  E            *4     !            [    �:)            �     @     �      k    �B)            9x    F)            �     X     �       Ԁ    @�)            �    �L)            0L  "   �     �          ��           �;    �T)            t"    �     C       Ǜ    �9     [      �!    ��     '      �_    @)            3g    �A)                ��     �       \    H?)                ��     k      (E  "  ��     E      |    G)            �$    `�     E      "j    �B)            d     A)            �    K)            ��    �M)            =u    XE)            �A     �     !       f=    �<)            |�    �K)            X    8>)            �    ��)             �  "  ��     %       e    PA)            \    �+     �       N;     �     �       ��    �L)            �*    �C     �      �+    �L     �      
      &�    �K)            �    �8            �  "  ��            �|    @G)            Tu    `E)            ?    �D)            \a    p@)            ;=    0�     �       QD  "  ��     �      ��    �J)            4�    �H)            �    0�     B       y	    p/           ;    0}     G      '5  "  P�     ~       ]�    M)            p'     ;)            _?    P�     �      e%    @,     X       �S  "  P"     �      �P  "  `     '      �(    ��     �      U    �F)            V    ��     C	      �>    0=)            #V    �=)            ԁ    hH)            _  "  ��     �       �    3     �      5    Pi     ,      O    �A)              ! 
     �     5       o    �C)            1    �A)            m    `C)            �~    �G)            ��    �H)            �%  "  ��     �      4    �             �  !   #)            Zb    �@)            �u    �E)            �	    8?)            y�    xM)            �1    @Q)     `       "[     ?)            �    pH)            @    ��     	       ��    �>     �       �    �?     �      �          �      �     M)            ��    0J)            ,e    XA)             __gmon_start__ _init _fini _ITM_deregisterTMCloneTable _ITM_registerTMCloneTable __cxa_finalize _Jv_RegisterClasses _Z14ColorRGBToHLSffffPfS_S_ _Z14ColorRGBToHLSiiiiPiS_S_ _Z14ColorHLSToRGBffffPfS_S_ _Z14ColorHLSToRGBiiiiPiS_S_ _Z10ColorBlendjjf _ZN8CTexFontC2Ev _ZN8CTexFontC1Ev _ZN8CTexFontD2Ev _ZdaPv _ZN8CTexFontD1Ev _Z14TwGenerateFontPKhiif g_ErrBadFontHeight g_TwMgr _ZN6CTwMgr12SetLastErrorEPKc _Znwm _Znam memset _ZdlPv _Unwind_Resume __assert_fail __gxx_personality_v0 _Z22TwGenerateDefaultFontsf g_DefaultSmallFont g_DefaultNormalFont g_DefaultLargeFont g_DefaultFixed1Font _Z20TwDeleteDefaultFontsv _ZN8ITwGraphD2Ev _ZTV8ITwGraph _ZN8ITwGraphD1Ev _ZN14CTwGraphOpenGL8DrawLineEiiiijb _ZN14CTwGraphOpenGL8DrawRectEiiiij _ZN14CTwGraphOpenGL9IsDrawingEv _ZN14CTwGraphOpenGL7RestoreEv _ZN2GL17_glDeleteTexturesE _ZN14CTwGraphOpenGL15RestoreViewportEv _ZN2GL11_glViewportE _ZN2GL14_glGetIntegervE _ZN2GL13_glMatrixModeE _ZN2GL14_glLoadMatrixfE _ZN14CTwGraphOpenGLD2Ev _ZN14CTwGraphOpenGLD1Ev _ZN8ITwGraphD0Ev _ZN14CTwGraphOpenGLD0Ev _ZN14CTwGraphOpenGL7EndDrawEv _ZN2GL14_glBindTextureE _glBindVertexArray _glBindBufferARB _glBindProgramARB _glGetHandleARB _glUseProgramObjectARB _glTexImage3D _glBlendEquation _glBlendEquationSeparate _glBlendFuncSeparate _ZN2GL14_glPolygonModeE _ZN2GL10_glTexEnviE _ZN2GL12_glLineWidthE _ZN2GL12_glPopMatrixE _ZN2GL18_glPopClientAttribE _ZN2GL12_glPopAttribE _glActiveTextureARB _ZN2GL9_glEnableE _glClientActiveTextureARB _ZN2GL20_glEnableClientStateE _glEnableVertexAttribArray _ZN14CTwGraphOpenGL8DrawLineEiiiijjb _ZN2GL10_glDisableE _ZN2GL15_glLoadIdentityE _ZN2GL8_glBeginE _ZN2GL11_glColor4ubE _ZN2GL11_glVertex2fE _ZN2GL6_glEndE _ZN14CTwGraphOpenGL8DrawRectEiiiijjjj _ZN14CTwGraphOpenGL13DrawTrianglesEiPiPjN8ITwGraph4CullE _ZN2GL12_glIsEnabledE _ZN2GL11_glCullFaceE _ZN2GL12_glFrontFaceE _ZN14CTwGraphOpenGL9BeginDrawEii _ZN2GL13_glPushAttribE _ZN2GL19_glPushClientAttribE _ZN2GL21_glDisableClientStateE _ZN2GL13_glPushMatrixE _ZN2GL8_glOrthoE _ZN2GL12_glGetFloatvE _ZN2GL12_glBlendFuncE _ZN2GL14_glGetTexEnvivE _glDisableVertexAttribArray _glGetVertexAttribiv _ZN2GL12_glGetStringE strstr _ZN14CTwGraphOpenGL10NewTextObjEv _ZN14CTwGraphOpenGL14ChangeViewportEiiiiii _ZN14CTwGraphOpenGL10SetScissorEiiii _ZN2GL10_glScissorE _ZN14CTwGraphOpenGL4InitEv _Z10LoadOpenGLv _ZN2GL17_glGetProcAddressE g_ErrCantLoadOGL _ZN14CTwGraphOpenGL4ShutEv _Z12UnloadOpenGLv g_ErrCantUnloadOGL _ZN14CTwGraphOpenGL8DrawTextEPviijj _ZN2GL13_glTranslatefE _ZN2GL16_glVertexPointerE _ZN2GL13_glDrawArraysE _ZN2GL18_glTexCoordPointerE _ZN2GL15_glColorPointerE _ZN14CTwGraphOpenGL13DeleteTextObjEPv _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIjSaIjEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPjS1_EERKj memmove _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZSt20__throw_length_errorPKc _ZN14CTwGraphOpenGL9BuildTextEPvPKSsPjS3_iPK8CTexFontii _ZN2GL14_glGenTexturesE _ZN2GL14_glPixelStoreiE _ZN2GL17_glPixelTransferfE _ZN2GL13_glTexImage2DE _ZN2GL16_glTexParameterfE _ZTI8ITwGraph _ZTVN10__cxxabiv117__class_type_infoE _ZTS8ITwGraph _ZTS14CTwGraphOpenGL _ZTI14CTwGraphOpenGL _ZTVN10__cxxabiv120__si_class_type_infoE __cxa_pure_virtual _ZTV14CTwGraphOpenGL g_LargeFontTexID g_NormalFontTexID g_SmallFontTexID _ZN18CTwGraphOpenGLCore8DrawLineEiiiijb _ZN18CTwGraphOpenGLCore8DrawRectEiiiij _ZN18CTwGraphOpenGLCore9IsDrawingEv _ZN18CTwGraphOpenGLCore7RestoreEv _ZN6GLCore17_glDeleteTexturesE _ZN18CTwGraphOpenGLCore14ChangeViewportEiiiiii _ZN18CTwGraphOpenGLCore15RestoreViewportEv _ZN18CTwGraphOpenGLCoreD2Ev _ZN18CTwGraphOpenGLCoreD1Ev _ZN18CTwGraphOpenGLCoreD0Ev _ZN18CTwGraphOpenGLCore9BeginDrawEii _ZN6GLCore14_glGetIntegervE _ZN6GLCore11_glViewportE _ZN6GLCore18_glBindVertexArrayE _ZN6GLCore12_glGetFloatvE _ZN6GLCore12_glLineWidthE _ZN6GLCore12_glIsEnabledE _ZN6GLCore10_glDisableE _ZN6GLCore9_glEnableE _ZN6GLCore12_glBlendFuncE _ZN6GLCore14_glBindTextureE _ZN6GLCore13_glUseProgramE _ZN6GLCore16_glActiveTextureE _ZN18CTwGraphOpenGLCore7EndDrawEv _ZN6GLCore10_glScissorE _ZN18CTwGraphOpenGLCore8DrawLineEiiiijjb _ZN6GLCore13_glBindBufferE _ZN6GLCore16_glBufferSubDataE _ZN6GLCore22_glVertexAttribPointerE _ZN6GLCore26_glEnableVertexAttribArrayE _ZN6GLCore13_glDrawArraysE _ZN18CTwGraphOpenGLCore8DrawRectEiiiijjjj _ZN18CTwGraphOpenGLCore4ShutEv _ZN6GLCore16_glDeleteProgramE _ZN6GLCore15_glDeleteShaderE _ZN6GLCore16_glDeleteBuffersE _ZN6GLCore21_glDeleteVertexArraysE _Z16UnloadOpenGLCorev _ZN18CTwGraphOpenGLCore10NewTextObjEv _ZN18CTwGraphOpenGLCore10SetScissorEiiii _ZN18CTwGraphOpenGLCore13DeleteTextObjEPv _ZN18CTwGraphOpenGLCore16ResizeTriBuffersEm _ZN6GLCore13_glBufferDataE _ZN6GLCore15_glCreateShaderE _ZN6GLCore15_glShaderSourceE _ZN6GLCore16_glCompileShaderE _ZN6GLCore14_glGetShaderivE _ZN6GLCore19_glGetShaderInfoLogE stderr fprintf _ZN6GLCore16_glCreateProgramE _ZN6GLCore15_glAttachShaderE _ZN6GLCore21_glBindAttribLocationE _ZN6GLCore14_glLinkProgramE _ZN6GLCore15_glGetProgramivE _ZN6GLCore20_glGetProgramInfoLogE _ZN6GLCore18_glGenVertexArraysE _ZN6GLCore13_glGenBuffersE _ZN6GLCore21_glGetUniformLocationE _ZN18CTwGraphOpenGLCore4InitEv _Z14LoadOpenGLCorev _ZN18CTwGraphOpenGLCore13DrawTrianglesEiPiPjN8ITwGraph4CullE _ZN6GLCore11_glCullFaceE _ZN6GLCore12_glFrontFaceE _ZN6GLCore12_glUniform2fE _ZN6GLCore27_glDisableVertexAttribArrayE _ZN18CTwGraphOpenGLCore8DrawTextEPviijj _ZN6GLCore12_glUniform4fE _ZN6GLCore12_glUniform1iE _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN18CTwGraphOpenGLCore9BuildTextEPvPKSsPjS3_iPK8CTexFontii _ZN6GLCore14_glGenTexturesE _ZN6GLCore14_glPixelStoreiE _ZN6GLCore13_glTexImage2DE _ZN6GLCore16_glTexParameterfE _ZTS18CTwGraphOpenGLCore _ZTI18CTwGraphOpenGLCore _ZTV18CTwGraphOpenGLCore _ZNK6CTwVar8IsCustomEv _ZNK10CTwVarAtom7IsGroupEv _ZNK10CTwVarAtom10IsReadOnlyEv _ZNK11CTwVarGroup7IsGroupEv _ZN11CTwVarGroup11SetReadOnlyEb _ZNK11CTwVarGroup10IsReadOnlyEv _ZNK10CTwVarAtom18MinMaxStepToDoubleEPdS0_S0_ _ZN11CTwVarGroup12FindShortcutEiiPb _ZNSt6vectorIcSaIcEED2Ev _ZNSt6vectorIcSaIcEED1Ev _ZNK10CTwVarAtom4FindEPKcPP11CTwVarGroupPi strcmp _ZN10CTwVarAtom11SetReadOnlyEb _ZNK6CTwVar9HasAttribEPKcPb strcasecmp _ZNK10CTwVarAtom9HasAttribEPKcPb _ZNK11CTwVarGroup9HasAttribEPKcPb _ZNSt9basic_iosIcSt11char_traitsIcEE5clearESt12_Ios_Iostate _ZNK11CTwVarGroup4FindEPKcPPS_Pi _ZNSs12_M_leak_hardEv _ZNSs6resizeEmc _ZSt20__throw_out_of_rangePKc _ZN6CTwVarD2Ev _ZTV6CTwVar _ZNSs4_Rep20_S_empty_rep_storageE _ZNSs4_Rep10_M_destroyERKSaIcE _ZN6CTwVarD1Ev _ZN6CTwVarD0Ev _ZN10CTwVarAtomD2Ev _ZTV10CTwVarAtom free _ZN6CTwMgr12CCDStdString5GetCBEPvS1_ _ZNSt8__detail15_List_node_base9_M_unhookEv _ZN10CTwVarAtomD1Ev _ZN10CTwVarAtomD0Ev _ZN11CTwVarGroupD2Ev _ZTV11CTwVarGroup _ZN11CTwVarGroupD1Ev _ZN11CTwVarGroupD0Ev _Z16Color32FromARGBiiiii _ZN9PerfTimer7GetTimeEv gettimeofday _Z12IsCustomTypei _ZNK10CTwVarAtom8IsCustomEv _Z14IsCSStringTypei _Z10IsEnumTypei _ZNK10CTwVarAtom13ValueToDoubleEv _ZN10CTwVarAtom15ValueFromDoubleEd _ZN10CTwVarAtom11SetDefaultsEv _ZN10CTwVarAtom9IncrementEi _ZSt18_Rb_tree_incrementPSt18_Rb_tree_node_base fwrite _ZSt18_Rb_tree_decrementPSt18_Rb_tree_node_base _ZN6CTwVarC2Ev _ZN6CTwVarC1Ev _ZN10CTwVarAtomC2Ev _ZN10CTwVarAtomC1Ev _ZN6CTwVar11GetDataSizeE7ETwType _ZNK6CTwBar4FindEPKcPP11CTwVarGroupPi _ZN6CTwBar4FindEPKcPP11CTwVarGroupPi _ZNK6CTwBar9HasAttribEPKcPb _ZN6CTwBar11NotUpToDateEv _ZN6CTwBar9SetAttribEiPKc _Z13TwSetBarStateP6CTwBar8ETwState g_ErrNoValue _ZNSs9_M_mutateEmmm _ZN6CTwMgr9SetAttribEiPKc sscanf strlen _ZNSs6assignEPKcm g_ErrBadValue g_ErrUnknownAttrib _ZNSs6assignERKSs TwSetBottomBar TwSetTopBar TwDeleteBar _ZN6CTwBar12UpdateColorsEv _ZN6CTwBar18ComputeLabelsWidthEPK8CTexFont _ZN6CTwBar18ComputeValuesWidthEPK8CTexFont _ZNSs4_Rep10_M_disposeERKSaIcE _ZN6CTwBar14DrawHierHandleEv _ZN6CTwBar8OpenHierEP11CTwVarGroupP6CTwVar _ZN6CTwBar10LineInHierEP11CTwVarGroupP6CTwVar _Z7DrawArciiiffj sincosf _ZN6CTwBar11CRotoSliderC2Ev _ZN6CTwBar11CRotoSliderC1Ev _ZNK6CTwBar12RotoGetValueEv _ZN6CTwBar12RotoSetValueEd _ZNK6CTwBar10RotoGetMinEv _ZNK6CTwBar10RotoGetMaxEv _ZNK6CTwBar11RotoGetStepEv _ZN6CTwBar8RotoDrawEv sincos _ZNK6CTwBar19RotoGetSteppedValueEv _ZN6CTwBar15RotoOnMouseMoveEii _ZN6CTwMgr9SetCursorEm atan2 acos sqrtf sqrt _ZN6CTwBar17RotoOnLButtonDownEii _ZN6CTwBar15RotoOnLButtonUpEii _ZN6CTwBar17RotoOnMButtonDownEii _ZN6CTwBar15RotoOnMButtonUpEii _ZN6CTwBar12CEditInPlaceC2Ev _ZN6CTwBar12CEditInPlaceC1Ev _ZN6CTwBar12CEditInPlaceD2Ev _ZN6CTwBar12CEditInPlaceD1Ev _ZN6CTwBar21EditInPlaceIsReadOnlyEv _ZN6CTwBar15EditInPlaceDrawEv _ZNSsC1ERKSsmm _ZNSsC1EPKcRKSaIcE _ZN6CTwBar20EditInPlaceAcceptVarEPK10CTwVarAtom _ZN6CTwBar14EditInPlaceEndEb strncpy _ZN6CTwBar16EditInPlaceStartEP10CTwVarAtomiii _ZN6CTwBar22EditInPlaceEraseSelectEv _ZN6CTwBar20EditInPlaceMouseMoveEiib _ZN6CTwBar23EditInPlaceGetClipboardEPSs XFetchBytes memcpy XFree _ZN6CTwBar23EditInPlaceSetClipboardERKSs XSetSelectionOwner XStoreBytes _ZN6CTwBar21EditInPlaceKeyPressedEii _ZNSsC1EmcRKSaIcE _ZNSs6insertEmPKcm _ZNSt6vectorIcSaIcEE6resizeEmc _ZNK10CTwVarAtom13ValueToStringEPSs sprintf _ZNSs7reserveEm _ZNSs6appendEmc _ZNSs6appendERKSs _ZNSsC1ERKSs _ZNSs6appendEPKcm _Z14TwGetKeyStringPSsii _ZNSs6assignEPKc _ZNSt6vectorIP6CTwVarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6CTwVar9SetAttribEiPKcP6CTwBarP11CTwVarGroupi _ZN9CColorExt9SummaryCBEPcmPKvPv TwRemoveVar g_ErrNotGroup _ZN11CTwVarGroup9SetAttribEiPKcP6CTwBarPS_i _ZN14CQuaternionExt9SummaryCBEPcmPKvPv _ZNSt6vectorIdSaIdEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPdS1_EERKd _ZNK6CTwVar9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZSt16__ostream_insertIcSt11char_traitsIcEERSt13basic_ostreamIT_T0_ES6_PKS3_l _ZNK11CTwVarGroup9GetAttribEiP6CTwBarPS_iRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE g_ErrInvalidAttrib _ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc _ZNK10CTwVarAtom9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNKSs13find_first_ofEPKcmm _ZNSo9_M_insertImEERSoT_ _ZNK6CTwBar9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNK6CTwMgr9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE memcmp _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE16_M_insert_uniqueERKS2_ _ZSt29_Rb_tree_insert_and_rebalancebPSt18_Rb_tree_node_baseS0_RS_ __cxa_begin_catch __cxa_rethrow __cxa_end_catch _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE8_M_eraseEPSt13_Rb_tree_nodeIS7_E _ZN6CTwBarC2EPKc _ZN6CTwBarC1EPKc _ZN6CTwBarD2Ev _ZN6CTwMgr8MaximizeEP6CTwBar _ZN6CTwBarD1Ev _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorISsSaISsEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPSsS1_EERKSs _ZN6CTwBar10ListLabelsERSt6vectorISsSaISsEERS0_IjSaIjEES6_PbPK8CTexFontii _ZN6CTwBar10ListValuesERSt6vectorISsSaISsEERS0_IjSaIjEES6_PK8CTexFonti _ZN6CTwMgr7CStruct14DefaultSummaryEPcmPKvPv _ZNKSs7compareEPKc __cxa_guard_acquire __cxa_guard_release __cxa_atexit _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE16_M_insert_uniqueERKS7_ _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE8_M_eraseEPSt13_Rb_tree_nodeIS2_E _ZN10CTwVarAtom9SetAttribEiPKcP6CTwBarP11CTwVarGroupi _Z12TwGetKeyCodePiS_PKc strdup _ZSt28_Rb_tree_rebalance_for_erasePSt18_Rb_tree_node_baseRS_ g_ErrUnknownType _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN6CTwBar15BrowseHierarchyEPiiPK6CTwVarii _ZNSt6vectorISsSaISsEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPSsS1_EEmRKSs _ZNSt6vectorIjSaIjEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPjS1_EEmRKj _ZN6CTwBar6UpdateEv g_BarTimer _ZN6CTwBar4DrawEi _ZN6CTwMgr13UpdateHelpBarEv _ZN6CTwBar11MouseMotionEii _ZN6CTwBar11MouseButtonE16ETwMouseButtonIDbii _ZN6CTwMgr8MinimizeEP6CTwBar TwNewBar TwAddButton _ZN6CTwMgr7SetFontEPK8CTexFontb _ZN6CTwBar10MouseWheelEiiii _ZN6CTwBar7KeyTestEii _ZN6CTwBar4ShowEP6CTwVar _ZN6CTwBar10KeyPressedEii _ZTS6CTwVar _ZTI6CTwVar _ZTS10CTwVarAtom _ZTI10CTwVarAtom _ZTS11CTwVarGroup _ZTI11CTwVarGroup g_ErrNotEnum __pthread_key_create _ZN14CQuaternionExt12MouseLeaveCBEPvS0_P6CTwBar _ZNSt6vectorIfSaIfEED2Ev _ZNSt6vectorIfSaIfEED1Ev _ZN14CQuaternionExt11s_ArrowNormE _ZN14CQuaternionExt10s_ArrowTriE _ZNSt6vectorIiSaIiEED2Ev _ZNSt6vectorIiSaIiEED1Ev _ZN14CQuaternionExt14s_ArrowTriProjE _ZNSt6vectorIjSaIjEED2Ev _ZNSt6vectorIjSaIjEED1Ev _ZN14CQuaternionExt15s_ArrowColLightE glXGetCurrentDisplay XSetErrorHandler XFlush XSync snprintf XAllocNamedColor XCreateBitmapFromData XCreatePixmapCursor XFreePixmap _ZN14CQuaternionExt13MouseButtonCBE16ETwMouseButtonIDbiiiiPvS1_P6CTwBarP11CTwVarGroup _ZN6CTwMgr12CMemberProxy5GetCBEPvS1_ _ZN6CTwMgr7CStruct23s_PassProxyAsClientDataE _ZNSt8__detail15_List_node_base7_M_hookEPS0_ _ZN9CColorExt13InitColor32CBEPvS0_ _ZN9CColorExt13InitColor3FCBEPvS0_ _ZN9CColorExt13InitColor4FCBEPvS0_ _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED2Ev _ZTVSt15basic_stringbufIcSt11char_traitsIcESaIcEE _ZTVSt15basic_streambufIcSt11char_traitsIcEE _ZNSt6localeD1Ev _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED1Ev _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED0Ev strncat _ZN9CColorExt7RGB2HLSEv _ZN9CColorExt14CopyVarToExtCBEPKvPvjS2_ _ZN9CColorExt7HLS2RGBEv _ZN9CColorExt16CopyVarFromExtCBEPvPKvjS0_ _ZN14CQuaternionExt18ConvertToAxisAngleEv sin _ZN14CQuaternionExt12InitQuat4FCBEPvS0_ _ZN14CQuaternionExt6DrawCBEiiPvS0_P6CTwBarP11CTwVarGroup _ZN14CQuaternionExt13MouseMotionCBEiiiiPvS0_P6CTwBarP11CTwVarGroup _ZN14CQuaternionExt12InitQuat4DCBEPvS0_ _ZN14CQuaternionExt11InitDir3FCBEPvS0_ _ZN14CQuaternionExt11InitDir3DCBEPvS0_ _ZN14CQuaternionExt20ConvertFromAxisAngleEv _ZN14CQuaternionExt16CopyVarFromExtCBEPvPKvjS0_ _ZN14CQuaternionExt9ApplyQuatEPfS0_S0_fffffff _ZN14CQuaternionExt9CopyToVarEv _ZN14CQuaternionExt11QuatFromDirEPdS0_S0_S0_ddd _ZN14CQuaternionExt14CopyVarToExtCBEPKvPvjS2_ _ZN14CQuaternionExt7PermuteEPfS0_S0_fff _ZN14CQuaternionExt8s_SphTriE _ZN14CQuaternionExt8s_SphColE _ZN14CQuaternionExt12s_SphTriProjE _ZN14CQuaternionExt13s_SphColLightE _ZN6CTwMgr16CClientStdStringC2Ev _ZN6CTwMgr16CClientStdStringC1Ev _ZN6CTwMgr16CClientStdString7FromLibEPKc _ZN6CTwMgr16CClientStdString8ToClientEv _ZN6CTwMgr12CCDStdString5SetCBEPKvPv _ZN6CTwMgr12CMemberProxy5SetCBEPKvPv _ZN6CTwMgr13CLibStdStringC2Ev _ZN6CTwMgr13CLibStdStringC1Ev _ZN6CTwMgr13CLibStdString10FromClientERKSs _ZN6CTwMgr13CLibStdString5ToLibEv __cxa_guard_abort TwWindowExists g_Wnds _ZN6CTwMgrC2E11ETwGraphAPIPvi g_InitWndWidth g_InitWndHeight g_InitCopyCDStringToClient g_InitCopyStdStringToClient _ZN6CTwMgrC1E11ETwGraphAPIPvi _ZNK6CTwMgr7FindBarEPKc _ZNK6CTwMgr9HasAttribEPKcPb _ZN6CTwMgr4HideEP6CTwBar _ZN6CTwMgr6UnhideEP6CTwBar _Z13TwGlobalErrorPKc g_ErrorHandler g_BreakOnError TwGetCurrentWindow g_ErrNotInit g_TwMasterMgr TwSetLastError g_ErrIsDrawing usleep TwWindowSize g_ErrBadSize _ZN6CTwMgr12GetLastErrorEv _ZNK6CTwMgr14CheckLastErrorEv _ZN6CTwMgr19SetCurrentDbgParamsEPKci _Z7__TwDbgPKci _Z14TwHandleErrorsPFvPKcEi TwHandleErrors TwGetLastError g_ErrNotFound g_ErrDelHelp g_ErrBadParam TwDeleteAllBars _ZSt17__throw_bad_allocv TwGetTopBar TW_MOUSE_WHEEL TW_MOUSE_MOTION TwGetBottomBar TwGetBarName TwGetBarCount TwGetBarByIndex g_ErrOutOfRange TwGetBarByName TwRefreshBar _ZN6CTwMgr12CStructProxyC2Ev _ZN6CTwMgr12CStructProxyC1Ev _ZN6CTwMgr12CStructProxyD2Ev _ZN6CTwMgr12CStructProxyD1Ev _ZN6CTwMgr18RestoreCDStdStringERKSt6vectorINS_18CCDStdStringRecordESaIS1_EE _ZN6CTwMgr12CMemberProxyC2Ev _ZN6CTwMgr12CMemberProxyC1Ev _ZN6CTwMgr12CMemberProxyD2Ev _ZN6CTwMgr12CMemberProxyD1Ev g_ErrDelStruct _Z10ParseTokenRSsPKcRiS2_bbcc g_TabLength _Z15BarVarHasAttribP6CTwBarP6CTwVarPKcPb _Z15BarVarSetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiPKc TwSetParam _ZNSt8ios_baseC2Ev _ZTVSt9basic_iosIcSt11char_traitsIcEE _ZTTSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt9basic_iosIcSt11char_traitsIcEE4initEPSt15basic_streambufIcS1_E _ZTVSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt6localeC1Ev g_ErrCStrParam _ZNSt19basic_ostringstreamIcSt11char_traitsIcESaIcEED1Ev g_ErrParse _ZNSolsEi _ZNKSt15basic_stringbufIcSt11char_traitsIcESaIcEE3strEv _ZNSt8ios_baseD2Ev _ZNSo9_M_insertIdEERSoT_ _ZN6CTwMgr7CStructD2Ev _ZN6CTwMgr7CStructD1Ev _ZN6CTwMgr13CStructMemberD2Ev _ZN6CTwMgr13CStructMemberD1Ev toupper _ZNSs9push_backEc TwMouseButton TwMouseMotion TW_MOUSE_NA TwMouseWheel TwKeyPressed TwKeyTest _ZN6CTwMgr12PixmapCursorEi _ZN6CTwMgr13CreateCursorsEv XCreateFontCursor _ZN6CTwMgr11FreeCursorsEv XFreeCursor glXGetCurrentDrawable XDefineCursor TwCopyCDStringToClientFunc TwCopyStdStringToClientFunc _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED2Ev _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED1Ev _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED2Ev _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED1Ev _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ES3_RKS6_ _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_PKS3_ _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_S8_ _ZNSt6vectorISsSaISsEED2Ev _ZNSt6vectorISsSaISsEED1Ev _ZNSt6vectorIPN6CTwMgr7CCustomESaIS2_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS2_S4_EERKS2_ _ZNSt6vectorIfSaIfEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPfS1_EERKf _ZN14CQuaternionExt12CreateSphereEv _ZNSt6vectorI5CRectSaIS0_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS0_S2_EERKS0_ _ZNK5CRect8SubtractERKS_RSt6vectorIS_SaIS_EE _ZNK5CRect8SubtractERKSt6vectorIS_SaIS_EERS2_ TwDraw _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE8_M_eraseEPSt13_Rb_tree_nodeIS6_E _ZNSt6vectorIdSaIdEE9push_backERKd _Z15BarVarGetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt6vectorIP6CTwBarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIiSaIiEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPiS1_EERKi _ZNSt6vectorIbSaIbEE13_M_insert_auxESt13_Bit_iteratorb strchr g_ErrNoBackQuote g_ErrExist _ZNSt6vectorIP6CTwVarSaIS1_EE9push_backERKS1_ _Z19GetBarVarFromStringPP6CTwBarPP6CTwVarPP11CTwVarGroupPiPKc TwDefine g_FontScaling g_ErrStdString TwAddVarRW TwAddVarCB _ZNSs6appendEPKc TwAddVarRO TwAddSeparator _ZN6CTwMgrD2Ev _ZN6CTwMgrD1Ev _ZN6CTwMgr5CEnumD2Ev _ZN6CTwMgr5CEnumD1Ev _ZN6CTwMgr7CStructC2ERKS0_ _ZN6CTwMgr7CStructC1ERKS0_ _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE16_M_insert_uniqueERKS0_ _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE8_M_eraseEPSt13_Rb_tree_nodeIS0_E _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE8_M_eraseEPSt13_Rb_tree_nodeIS4_E TwTerminate _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED2Ev _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED1Ev _ZNSt6vectorIiSaIiEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPiS1_EEmRKi _ZN14CQuaternionExt11CreateArrowEv _ZNSt6vectorIP6CTwVarSaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ TwRemoveAllVars _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EEaSERKS3_ _ZSt18_Rb_tree_incrementPKSt18_Rb_tree_node_base _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE17_M_insert_unique_ESt23_Rb_tree_const_iteratorIS6_ERKS6_ _ZNSt6vectorIcSaIcEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPcS1_EEmRKc TwCopyStdStringToLibrary TwCopyCDStringToLibrary _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE29_M_get_insert_hint_unique_posESt23_Rb_tree_const_iteratorIS4_ERS1_ _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE7_M_copyEPKSt13_Rb_tree_nodeIS2_EPSA_ _ZN6CTwMgr5CEnumC2ERKS0_ _ZN6CTwMgr5CEnumC1ERKS0_ _ZNSs12_S_constructIPcEES0_T_S1_RKSaIcESt20forward_iterator_tag _ZNSs4_Rep9_S_createEmmRKSaIcE _ZSt19__throw_logic_errorPKc TwGetParam g_ErrHasNoValue g_ErrBadType _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr18CCDStdStringRecordES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6CTwMgr17UnrollCDStdStringERSt6vectorINS_18CCDStdStringRecordESaIS1_EE7ETwTypePv _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr5CEnumES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr5CEnumESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ TwDefineEnum TwDefineEnumFromString _ZTTSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZTVSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZNSsC1EPKcmRKSaIcE _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEE7_M_syncEPcmm _ZSt7getlineIcSt11char_traitsIcESaIcEERSt13basic_istreamIT_T0_ES7_RSbIS4_S5_T1_ES4_ _ZNKSs17find_first_not_ofEPKcmm _ZNKSs16find_last_not_ofEPKcmm _ZNSt18basic_stringstreamIcSt11char_traitsIcESaIcEED1Ev _ZNSdD2Ev _ZNSt22__uninitialized_fill_nILb0EE15__uninit_fill_nIPN6CTwMgr13CStructMemberEmS3_EEvT_T0_RKT1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr13CStructMemberES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr7CStructES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ TwDefineStruct g_ErrOffset _Z17TwDefineStructExtPKcPK15CTwStructMemberjmmPFvPvS4_EPFvS4_PKvjS4_EPFvS8_S4_jS4_EPFvPcmS8_S4_ES4_S0_ _ZN9CColorExt11CreateTypesEv _ZN14CQuaternionExt11CreateTypesEv _ZN14CQuaternionExt12s_CustomTypeE TwInit g_ErrInit g_ErrUnknownAPI TwSetCurrentWindow g_ErrIsProcessing g_ErrNthToDo g_ErrBadDevice g_ErrShut glXGetProcAddressARB g_OGLFuncRec glViewport glVertexPointer _ZN2GL12_glVertex4svE glVertex4sv _ZN2GL11_glVertex4sE glVertex4s _ZN2GL12_glVertex4ivE glVertex4iv _ZN2GL11_glVertex4iE glVertex4i _ZN2GL12_glVertex4fvE glVertex4fv _ZN2GL11_glVertex4fE glVertex4f _ZN2GL12_glVertex4dvE glVertex4dv _ZN2GL11_glVertex4dE glVertex4d _ZN2GL12_glVertex3svE glVertex3sv _ZN2GL11_glVertex3sE glVertex3s _ZN2GL12_glVertex3ivE glVertex3iv _ZN2GL11_glVertex3iE glVertex3i _ZN2GL12_glVertex3fvE glVertex3fv _ZN2GL11_glVertex3fE glVertex3f _ZN2GL12_glVertex3dvE glVertex3dv _ZN2GL11_glVertex3dE glVertex3d _ZN2GL12_glVertex2svE glVertex2sv _ZN2GL11_glVertex2sE glVertex2s _ZN2GL12_glVertex2ivE glVertex2iv _ZN2GL11_glVertex2iE glVertex2i _ZN2GL12_glVertex2fvE glVertex2fv glVertex2f _ZN2GL12_glVertex2dvE glVertex2dv _ZN2GL11_glVertex2dE glVertex2d glTranslatef _ZN2GL13_glTranslatedE glTranslated _ZN2GL16_glTexSubImage2DE glTexSubImage2D _ZN2GL16_glTexSubImage1DE glTexSubImage1D _ZN2GL17_glTexParameterivE glTexParameteriv _ZN2GL16_glTexParameteriE glTexParameteri _ZN2GL17_glTexParameterfvE glTexParameterfv glTexParameterf glTexImage2D _ZN2GL13_glTexImage1DE glTexImage1D _ZN2GL11_glTexGenivE glTexGeniv _ZN2GL10_glTexGeniE glTexGeni _ZN2GL11_glTexGenfvE glTexGenfv _ZN2GL10_glTexGenfE glTexGenf _ZN2GL11_glTexGendvE glTexGendv _ZN2GL10_glTexGendE glTexGend _ZN2GL11_glTexEnvivE glTexEnviv glTexEnvi _ZN2GL11_glTexEnvfvE glTexEnvfv _ZN2GL10_glTexEnvfE glTexEnvf glTexCoordPointer _ZN2GL14_glTexCoord4svE glTexCoord4sv _ZN2GL13_glTexCoord4sE glTexCoord4s _ZN2GL14_glTexCoord4ivE glTexCoord4iv _ZN2GL13_glTexCoord4iE glTexCoord4i _ZN2GL14_glTexCoord4fvE glTexCoord4fv _ZN2GL13_glTexCoord4fE glTexCoord4f _ZN2GL14_glTexCoord4dvE glTexCoord4dv _ZN2GL13_glTexCoord4dE glTexCoord4d _ZN2GL14_glTexCoord3svE glTexCoord3sv _ZN2GL13_glTexCoord3sE glTexCoord3s _ZN2GL14_glTexCoord3ivE glTexCoord3iv _ZN2GL13_glTexCoord3iE glTexCoord3i _ZN2GL14_glTexCoord3fvE glTexCoord3fv _ZN2GL13_glTexCoord3fE glTexCoord3f _ZN2GL14_glTexCoord3dvE glTexCoord3dv _ZN2GL13_glTexCoord3dE glTexCoord3d _ZN2GL14_glTexCoord2svE glTexCoord2sv _ZN2GL13_glTexCoord2sE glTexCoord2s _ZN2GL14_glTexCoord2ivE glTexCoord2iv _ZN2GL13_glTexCoord2iE glTexCoord2i _ZN2GL14_glTexCoord2fvE glTexCoord2fv _ZN2GL13_glTexCoord2fE glTexCoord2f _ZN2GL14_glTexCoord2dvE glTexCoord2dv _ZN2GL13_glTexCoord2dE glTexCoord2d _ZN2GL14_glTexCoord1svE glTexCoord1sv _ZN2GL13_glTexCoord1sE glTexCoord1s _ZN2GL14_glTexCoord1ivE glTexCoord1iv _ZN2GL13_glTexCoord1iE glTexCoord1i _ZN2GL14_glTexCoord1fvE glTexCoord1fv _ZN2GL13_glTexCoord1fE glTexCoord1f _ZN2GL14_glTexCoord1dvE glTexCoord1dv _ZN2GL13_glTexCoord1dE glTexCoord1d _ZN2GL12_glStencilOpE glStencilOp _ZN2GL14_glStencilMaskE glStencilMask _ZN2GL14_glStencilFuncE glStencilFunc _ZN2GL13_glShadeModelE glShadeModel _ZN2GL15_glSelectBufferE glSelectBuffer glScissor _ZN2GL9_glScalefE glScalef _ZN2GL9_glScaledE glScaled _ZN2GL10_glRotatefE glRotatef _ZN2GL10_glRotatedE glRotated _ZN2GL13_glRenderModeE glRenderMode _ZN2GL9_glRectsvE glRectsv _ZN2GL8_glRectsE glRects _ZN2GL9_glRectivE glRectiv _ZN2GL8_glRectiE glRecti _ZN2GL9_glRectfvE glRectfv _ZN2GL8_glRectfE glRectf _ZN2GL9_glRectdvE glRectdv _ZN2GL8_glRectdE glRectd _ZN2GL13_glReadPixelsE glReadPixels _ZN2GL13_glReadBufferE glReadBuffer _ZN2GL15_glRasterPos4svE glRasterPos4sv _ZN2GL14_glRasterPos4sE glRasterPos4s _ZN2GL15_glRasterPos4ivE glRasterPos4iv _ZN2GL14_glRasterPos4iE glRasterPos4i _ZN2GL15_glRasterPos4fvE glRasterPos4fv _ZN2GL14_glRasterPos4fE glRasterPos4f _ZN2GL15_glRasterPos4dvE glRasterPos4dv _ZN2GL14_glRasterPos4dE glRasterPos4d _ZN2GL15_glRasterPos3svE glRasterPos3sv _ZN2GL14_glRasterPos3sE glRasterPos3s _ZN2GL15_glRasterPos3ivE glRasterPos3iv _ZN2GL14_glRasterPos3iE glRasterPos3i _ZN2GL15_glRasterPos3fvE glRasterPos3fv _ZN2GL14_glRasterPos3fE glRasterPos3f _ZN2GL15_glRasterPos3dvE glRasterPos3dv _ZN2GL14_glRasterPos3dE glRasterPos3d _ZN2GL15_glRasterPos2svE glRasterPos2sv _ZN2GL14_glRasterPos2sE glRasterPos2s _ZN2GL15_glRasterPos2ivE glRasterPos2iv _ZN2GL14_glRasterPos2iE glRasterPos2i _ZN2GL15_glRasterPos2fvE glRasterPos2fv _ZN2GL14_glRasterPos2fE glRasterPos2f _ZN2GL15_glRasterPos2dvE glRasterPos2dv _ZN2GL14_glRasterPos2dE glRasterPos2d _ZN2GL11_glPushNameE glPushName glPushMatrix glPushClientAttrib glPushAttrib _ZN2GL21_glPrioritizeTexturesE glPrioritizeTextures _ZN2GL10_glPopNameE glPopName glPopMatrix glPopClientAttrib glPopAttrib _ZN2GL17_glPolygonStippleE glPolygonStipple _ZN2GL16_glPolygonOffsetE glPolygonOffset glPolygonMode _ZN2GL12_glPointSizeE glPointSize _ZN2GL12_glPixelZoomE glPixelZoom _ZN2GL17_glPixelTransferiE glPixelTransferi glPixelTransferf glPixelStorei _ZN2GL14_glPixelStorefE glPixelStoref _ZN2GL14_glPixelMapusvE glPixelMapusv _ZN2GL14_glPixelMapuivE glPixelMapuiv _ZN2GL13_glPixelMapfvE glPixelMapfv _ZN2GL14_glPassThroughE glPassThrough glOrtho _ZN2GL16_glNormalPointerE glNormalPointer _ZN2GL12_glNormal3svE glNormal3sv _ZN2GL11_glNormal3sE glNormal3s _ZN2GL12_glNormal3ivE glNormal3iv _ZN2GL11_glNormal3iE glNormal3i _ZN2GL12_glNormal3fvE glNormal3fv _ZN2GL11_glNormal3fE glNormal3f _ZN2GL12_glNormal3dvE glNormal3dv _ZN2GL11_glNormal3dE glNormal3d _ZN2GL12_glNormal3bvE glNormal3bv _ZN2GL11_glNormal3bE glNormal3b _ZN2GL10_glNewListE glNewList _ZN2GL14_glMultMatrixfE glMultMatrixf _ZN2GL14_glMultMatrixdE glMultMatrixd glMatrixMode _ZN2GL13_glMaterialivE glMaterialiv _ZN2GL12_glMaterialiE glMateriali _ZN2GL13_glMaterialfvE glMaterialfv _ZN2GL12_glMaterialfE glMaterialf _ZN2GL12_glMapGrid2fE glMapGrid2f _ZN2GL12_glMapGrid2dE glMapGrid2d _ZN2GL12_glMapGrid1fE glMapGrid1f _ZN2GL12_glMapGrid1dE glMapGrid1d _ZN2GL8_glMap2fE glMap2f _ZN2GL8_glMap2dE glMap2d _ZN2GL8_glMap1fE glMap1f _ZN2GL8_glMap1dE glMap1d _ZN2GL10_glLogicOpE glLogicOp _ZN2GL11_glLoadNameE glLoadName glLoadMatrixf _ZN2GL14_glLoadMatrixdE glLoadMatrixd glLoadIdentity _ZN2GL11_glListBaseE glListBase glLineWidth _ZN2GL14_glLineStippleE glLineStipple _ZN2GL10_glLightivE glLightiv _ZN2GL9_glLightiE glLighti _ZN2GL10_glLightfvE glLightfv _ZN2GL9_glLightfE glLightf _ZN2GL15_glLightModelivE glLightModeliv _ZN2GL14_glLightModeliE glLightModeli _ZN2GL15_glLightModelfvE glLightModelfv _ZN2GL14_glLightModelfE glLightModelf _ZN2GL12_glIsTextureE glIsTexture _ZN2GL9_glIsListE glIsList glIsEnabled _ZN2GL20_glInterleavedArraysE glInterleavedArrays _ZN2GL12_glInitNamesE glInitNames _ZN2GL11_glIndexubvE glIndexubv _ZN2GL10_glIndexubE glIndexub _ZN2GL10_glIndexsvE glIndexsv _ZN2GL9_glIndexsE glIndexs _ZN2GL10_glIndexivE glIndexiv _ZN2GL9_glIndexiE glIndexi _ZN2GL10_glIndexfvE glIndexfv _ZN2GL9_glIndexfE glIndexf _ZN2GL10_glIndexdvE glIndexdv _ZN2GL9_glIndexdE glIndexd _ZN2GL15_glIndexPointerE glIndexPointer _ZN2GL12_glIndexMaskE glIndexMask _ZN2GL7_glHintE glHint _ZN2GL20_glGetTexParameterivE glGetTexParameteriv _ZN2GL20_glGetTexParameterfvE glGetTexParameterfv _ZN2GL25_glGetTexLevelParameterivE glGetTexLevelParameteriv _ZN2GL25_glGetTexLevelParameterfvE glGetTexLevelParameterfv _ZN2GL14_glGetTexImageE glGetTexImage _ZN2GL14_glGetTexGenivE glGetTexGeniv _ZN2GL14_glGetTexGenfvE glGetTexGenfv _ZN2GL14_glGetTexGendvE glGetTexGendv glGetTexEnviv _ZN2GL14_glGetTexEnvfvE glGetTexEnvfv glGetString _ZN2GL20_glGetPolygonStippleE glGetPolygonStipple _ZN2GL14_glGetPointervE glGetPointerv _ZN2GL17_glGetPixelMapusvE glGetPixelMapusv _ZN2GL17_glGetPixelMapuivE glGetPixelMapuiv _ZN2GL16_glGetPixelMapfvE glGetPixelMapfv _ZN2GL16_glGetMaterialivE glGetMaterialiv _ZN2GL16_glGetMaterialfvE glGetMaterialfv _ZN2GL11_glGetMapivE glGetMapiv _ZN2GL11_glGetMapfvE glGetMapfv _ZN2GL11_glGetMapdvE glGetMapdv _ZN2GL13_glGetLightivE glGetLightiv _ZN2GL13_glGetLightfvE glGetLightfv glGetIntegerv glGetFloatv _ZN2GL11_glGetErrorE glGetError _ZN2GL13_glGetDoublevE glGetDoublev _ZN2GL15_glGetClipPlaneE glGetClipPlane _ZN2GL14_glGetBooleanvE glGetBooleanv glGenTextures _ZN2GL11_glGenListsE glGenLists _ZN2GL10_glFrustumE glFrustum glFrontFace _ZN2GL8_glFogivE glFogiv _ZN2GL7_glFogiE glFogi _ZN2GL8_glFogfvE glFogfv _ZN2GL7_glFogfE glFogf _ZN2GL8_glFlushE glFlush _ZN2GL9_glFinishE glFinish _ZN2GL17_glFeedbackBufferE glFeedbackBuffer _ZN2GL13_glEvalPoint2E glEvalPoint2 _ZN2GL13_glEvalPoint1E glEvalPoint1 _ZN2GL12_glEvalMesh2E glEvalMesh2 _ZN2GL12_glEvalMesh1E glEvalMesh1 _ZN2GL15_glEvalCoord2fvE glEvalCoord2fv _ZN2GL14_glEvalCoord2fE glEvalCoord2f _ZN2GL15_glEvalCoord2dvE glEvalCoord2dv _ZN2GL14_glEvalCoord2dE glEvalCoord2d _ZN2GL15_glEvalCoord1fvE glEvalCoord1fv _ZN2GL14_glEvalCoord1fE glEvalCoord1f _ZN2GL15_glEvalCoord1dvE glEvalCoord1dv _ZN2GL14_glEvalCoord1dE glEvalCoord1d _ZN2GL10_glEndListE glEndList glEnd glEnableClientState glEnable _ZN2GL12_glEdgeFlagvE glEdgeFlagv _ZN2GL18_glEdgeFlagPointerE glEdgeFlagPointer _ZN2GL11_glEdgeFlagE glEdgeFlag _ZN2GL13_glDrawPixelsE glDrawPixels _ZN2GL15_glDrawElementsE glDrawElements _ZN2GL13_glDrawBufferE glDrawBuffer glDrawArrays glDisableClientState glDisable _ZN2GL13_glDepthRangeE glDepthRange _ZN2GL12_glDepthMaskE glDepthMask _ZN2GL12_glDepthFuncE glDepthFunc glDeleteTextures _ZN2GL14_glDeleteListsE glDeleteLists glCullFace _ZN2GL20_glCopyTexSubImage2DE glCopyTexSubImage2D _ZN2GL20_glCopyTexSubImage1DE glCopyTexSubImage1D _ZN2GL17_glCopyTexImage2DE glCopyTexImage2D _ZN2GL17_glCopyTexImage1DE glCopyTexImage1D _ZN2GL13_glCopyPixelsE glCopyPixels glColorPointer _ZN2GL16_glColorMaterialE glColorMaterial _ZN2GL12_glColorMaskE glColorMask _ZN2GL12_glColor4usvE glColor4usv _ZN2GL11_glColor4usE glColor4us _ZN2GL12_glColor4uivE glColor4uiv _ZN2GL11_glColor4uiE glColor4ui _ZN2GL12_glColor4ubvE glColor4ubv glColor4ub _ZN2GL11_glColor4svE glColor4sv _ZN2GL10_glColor4sE glColor4s _ZN2GL11_glColor4ivE glColor4iv _ZN2GL10_glColor4iE glColor4i _ZN2GL11_glColor4fvE glColor4fv _ZN2GL10_glColor4fE glColor4f _ZN2GL11_glColor4dvE glColor4dv _ZN2GL10_glColor4dE glColor4d _ZN2GL11_glColor4bvE glColor4bv _ZN2GL10_glColor4bE glColor4b _ZN2GL12_glColor3usvE glColor3usv _ZN2GL11_glColor3usE glColor3us _ZN2GL12_glColor3uivE glColor3uiv _ZN2GL11_glColor3uiE glColor3ui _ZN2GL12_glColor3ubvE glColor3ubv _ZN2GL11_glColor3ubE glColor3ub _ZN2GL11_glColor3svE glColor3sv _ZN2GL10_glColor3sE glColor3s _ZN2GL11_glColor3ivE glColor3iv _ZN2GL10_glColor3iE glColor3i _ZN2GL11_glColor3fvE glColor3fv _ZN2GL10_glColor3fE glColor3f _ZN2GL11_glColor3dvE glColor3dv _ZN2GL10_glColor3dE glColor3d _ZN2GL11_glColor3bvE glColor3bv _ZN2GL10_glColor3bE glColor3b _ZN2GL12_glClipPlaneE glClipPlane _ZN2GL15_glClearStencilE glClearStencil _ZN2GL13_glClearIndexE glClearIndex _ZN2GL13_glClearDepthE glClearDepth _ZN2GL13_glClearColorE glClearColor _ZN2GL13_glClearAccumE glClearAccum _ZN2GL8_glClearE glClear _ZN2GL12_glCallListsE glCallLists _ZN2GL11_glCallListE glCallList glBlendFunc _ZN2GL9_glBitmapE glBitmap glBindTexture glBegin _ZN2GL15_glArrayElementE glArrayElement _ZN2GL22_glAreTexturesResidentE glAreTexturesResident _ZN2GL12_glAlphaFuncE glAlphaFunc _ZN2GL8_glAccumE glAccum g_NbOGLFunc _ZN6GLCore17_glGetProcAddressE _ZN6GLCore16_glIsVertexArrayE g_OGLCoreFuncRec glVertexAttribPointer _ZN6GLCore19_glVertexAttrib4usvE glVertexAttrib4usv _ZN6GLCore19_glVertexAttrib4uivE glVertexAttrib4uiv _ZN6GLCore19_glVertexAttrib4ubvE glVertexAttrib4ubv _ZN6GLCore18_glVertexAttrib4svE glVertexAttrib4sv _ZN6GLCore17_glVertexAttrib4sE glVertexAttrib4s _ZN6GLCore18_glVertexAttrib4ivE glVertexAttrib4iv _ZN6GLCore18_glVertexAttrib4fvE glVertexAttrib4fv _ZN6GLCore17_glVertexAttrib4fE glVertexAttrib4f _ZN6GLCore18_glVertexAttrib4dvE glVertexAttrib4dv _ZN6GLCore17_glVertexAttrib4dE glVertexAttrib4d _ZN6GLCore18_glVertexAttrib4bvE glVertexAttrib4bv _ZN6GLCore20_glVertexAttrib4NusvE glVertexAttrib4Nusv _ZN6GLCore20_glVertexAttrib4NuivE glVertexAttrib4Nuiv _ZN6GLCore20_glVertexAttrib4NubvE glVertexAttrib4Nubv _ZN6GLCore19_glVertexAttrib4NubE glVertexAttrib4Nub _ZN6GLCore19_glVertexAttrib4NsvE glVertexAttrib4Nsv _ZN6GLCore19_glVertexAttrib4NivE glVertexAttrib4Niv _ZN6GLCore19_glVertexAttrib4NbvE glVertexAttrib4Nbv _ZN6GLCore18_glVertexAttrib3svE glVertexAttrib3sv _ZN6GLCore17_glVertexAttrib3sE glVertexAttrib3s _ZN6GLCore18_glVertexAttrib3fvE glVertexAttrib3fv _ZN6GLCore17_glVertexAttrib3fE glVertexAttrib3f _ZN6GLCore18_glVertexAttrib3dvE glVertexAttrib3dv _ZN6GLCore17_glVertexAttrib3dE glVertexAttrib3d _ZN6GLCore18_glVertexAttrib2svE glVertexAttrib2sv _ZN6GLCore17_glVertexAttrib2sE glVertexAttrib2s _ZN6GLCore18_glVertexAttrib2fvE glVertexAttrib2fv _ZN6GLCore17_glVertexAttrib2fE glVertexAttrib2f _ZN6GLCore18_glVertexAttrib2dvE glVertexAttrib2dv _ZN6GLCore17_glVertexAttrib2dE glVertexAttrib2d _ZN6GLCore18_glVertexAttrib1svE glVertexAttrib1sv _ZN6GLCore17_glVertexAttrib1sE glVertexAttrib1s _ZN6GLCore18_glVertexAttrib1fvE glVertexAttrib1fv _ZN6GLCore17_glVertexAttrib1fE glVertexAttrib1f _ZN6GLCore18_glVertexAttrib1dvE glVertexAttrib1dv _ZN6GLCore17_glVertexAttrib1dE glVertexAttrib1d _ZN6GLCore18_glValidateProgramE glValidateProgram _ZN6GLCore19_glUniformMatrix4fvE glUniformMatrix4fv _ZN6GLCore19_glUniformMatrix3fvE glUniformMatrix3fv _ZN6GLCore19_glUniformMatrix2fvE glUniformMatrix2fv _ZN6GLCore13_glUniform4ivE glUniform4iv _ZN6GLCore13_glUniform3ivE glUniform3iv _ZN6GLCore13_glUniform2ivE glUniform2iv _ZN6GLCore13_glUniform1ivE glUniform1iv _ZN6GLCore13_glUniform4fvE glUniform4fv _ZN6GLCore13_glUniform3fvE glUniform3fv _ZN6GLCore13_glUniform2fvE glUniform2fv _ZN6GLCore13_glUniform1fvE glUniform1fv _ZN6GLCore12_glUniform4iE glUniform4i _ZN6GLCore12_glUniform3iE glUniform3i _ZN6GLCore12_glUniform2iE glUniform2i glUniform1i glUniform4f _ZN6GLCore12_glUniform3fE glUniform3f glUniform2f _ZN6GLCore12_glUniform1fE glUniform1f glUseProgram glShaderSource glLinkProgram _ZN6GLCore11_glIsShaderE glIsShader _ZN6GLCore12_glIsProgramE glIsProgram _ZN6GLCore26_glGetVertexAttribPointervE glGetVertexAttribPointerv _ZN6GLCore20_glGetVertexAttribivE _ZN6GLCore20_glGetVertexAttribfvE glGetVertexAttribfv _ZN6GLCore20_glGetVertexAttribdvE glGetVertexAttribdv _ZN6GLCore15_glGetUniformivE glGetUniformiv _ZN6GLCore15_glGetUniformfvE glGetUniformfv glGetUniformLocation _ZN6GLCore18_glGetShaderSourceE glGetShaderSource glGetShaderInfoLog glGetShaderiv glGetProgramInfoLog glGetProgramiv _ZN6GLCore20_glGetAttribLocationE glGetAttribLocation _ZN6GLCore21_glGetAttachedShadersE glGetAttachedShaders _ZN6GLCore19_glGetActiveUniformE glGetActiveUniform _ZN6GLCore18_glGetActiveAttribE glGetActiveAttrib _ZN6GLCore15_glDetachShaderE glDetachShader glDeleteShader glDeleteProgram glCreateShader glCreateProgram glCompileShader glBindAttribLocation glAttachShader _ZN6GLCore22_glStencilMaskSeparateE glStencilMaskSeparate _ZN6GLCore22_glStencilFuncSeparateE glStencilFuncSeparate _ZN6GLCore20_glStencilOpSeparateE glStencilOpSeparate _ZN6GLCore14_glDrawBuffersE glDrawBuffers _ZN6GLCore24_glBlendEquationSeparateE _ZN6GLCore20_glGetBufferPointervE glGetBufferPointerv _ZN6GLCore23_glGetBufferParameterivE glGetBufferParameteriv _ZN6GLCore14_glUnmapBufferE glUnmapBuffer _ZN6GLCore12_glMapBufferE glMapBuffer _ZN6GLCore19_glGetBufferSubDataE glGetBufferSubData glBufferSubData glBufferData _ZN6GLCore11_glIsBufferE glIsBuffer glGenBuffers glDeleteBuffers glBindBuffer _ZN6GLCore20_glGetQueryObjectuivE glGetQueryObjectuiv _ZN6GLCore19_glGetQueryObjectivE glGetQueryObjectiv _ZN6GLCore13_glGetQueryivE glGetQueryiv _ZN6GLCore11_glEndQueryE glEndQuery _ZN6GLCore13_glBeginQueryE glBeginQuery _ZN6GLCore10_glIsQueryE glIsQuery _ZN6GLCore16_glDeleteQueriesE glDeleteQueries _ZN6GLCore13_glGenQueriesE glGenQueries _ZN6GLCore19_glPointParameterivE glPointParameteriv _ZN6GLCore18_glPointParameteriE glPointParameteri _ZN6GLCore19_glPointParameterfvE glPointParameterfv _ZN6GLCore18_glPointParameterfE glPointParameterf _ZN6GLCore20_glMultiDrawElementsE glMultiDrawElements _ZN6GLCore18_glMultiDrawArraysE glMultiDrawArrays _ZN6GLCore20_glBlendFuncSeparateE _ZN6GLCore24_glGetCompressedTexImageE glGetCompressedTexImage _ZN6GLCore26_glCompressedTexSubImage1DE glCompressedTexSubImage1D _ZN6GLCore26_glCompressedTexSubImage2DE glCompressedTexSubImage2D _ZN6GLCore26_glCompressedTexSubImage3DE glCompressedTexSubImage3D _ZN6GLCore23_glCompressedTexImage1DE glCompressedTexImage1D _ZN6GLCore23_glCompressedTexImage2DE glCompressedTexImage2D _ZN6GLCore23_glCompressedTexImage3DE glCompressedTexImage3D _ZN6GLCore17_glSampleCoverageE glSampleCoverage glActiveTexture _ZN6GLCore20_glCopyTexSubImage3DE glCopyTexSubImage3D _ZN6GLCore16_glTexSubImage3DE glTexSubImage3D _ZN6GLCore13_glTexImage3DE _ZN6GLCore20_glDrawRangeElementsE glDrawRangeElements _ZN6GLCore16_glBlendEquationE _ZN6GLCore13_glBlendColorE glBlendColor _ZN6GLCore12_glIsTextureE _ZN6GLCore16_glTexSubImage2DE _ZN6GLCore16_glTexSubImage1DE _ZN6GLCore20_glCopyTexSubImage2DE _ZN6GLCore20_glCopyTexSubImage1DE _ZN6GLCore17_glCopyTexImage2DE _ZN6GLCore17_glCopyTexImage1DE _ZN6GLCore16_glPolygonOffsetE _ZN6GLCore14_glGetPointervE _ZN6GLCore15_glDrawElementsE _ZN6GLCore13_glDepthRangeE _ZN6GLCore25_glGetTexLevelParameterivE _ZN6GLCore25_glGetTexLevelParameterfvE _ZN6GLCore20_glGetTexParameterivE _ZN6GLCore20_glGetTexParameterfvE _ZN6GLCore14_glGetTexImageE _ZN6GLCore12_glGetStringE _ZN6GLCore11_glGetErrorE _ZN6GLCore13_glGetDoublevE _ZN6GLCore14_glGetBooleanvE _ZN6GLCore13_glReadPixelsE _ZN6GLCore13_glReadBufferE _ZN6GLCore14_glPixelStorefE _ZN6GLCore12_glDepthFuncE _ZN6GLCore12_glStencilOpE _ZN6GLCore14_glStencilFuncE _ZN6GLCore10_glLogicOpE _ZN6GLCore8_glFlushE _ZN6GLCore9_glFinishE _ZN6GLCore12_glDepthMaskE _ZN6GLCore12_glColorMaskE _ZN6GLCore14_glStencilMaskE _ZN6GLCore13_glClearDepthE _ZN6GLCore15_glClearStencilE _ZN6GLCore13_glClearColorE _ZN6GLCore8_glClearE _ZN6GLCore13_glDrawBufferE _ZN6GLCore13_glTexImage1DE _ZN6GLCore17_glTexParameterivE _ZN6GLCore16_glTexParameteriE _ZN6GLCore17_glTexParameterfvE _ZN6GLCore14_glPolygonModeE _ZN6GLCore12_glPointSizeE _ZN6GLCore7_glHintE g_NbOGLCoreFunc TwEventMouseButtonGLFW TwEventKeyGLFW g_KMod TwEventCharGLFW TwEventMouseButtonGLFWcdecl TwEventKeyGLFWcdecl TwEventCharGLFWcdecl TwEventMousePosGLFWcdecl TwEventMouseWheelGLFWcdecl TwEventMouseButtonGLUT TwEventMouseMotionGLUT TwGLUTModifiersFunc g_GLUTGetModifiers TwEventKeyboardGLUT TwEventSpecialGLUT TwEventSDL TwEventSDL13 TwEventSDL12 TwEventSFML TwEventX11 XLookupString buff_sz libgcc_s.so.1 libc.so.6 _edata __bss_start _end libAntTweakBar.so.1 GCC_3.0 GLIBC_2.14 GLIBC_2.2.5                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   4�         P&y   x�        B�         ���   ��     ui	   ��      �")            ��     �")            `�     �")            p�     �")            ��     �")            ��     �")            P�      #)             #)     �:)            �H     �:)            ��     �:)            8�     �:)            �	     �:)            �	     �:)            �	      ;)            �	     ;)            �	      ;)            �	     0;)            
     @;)            �     X;)            �     `;)            �     x;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �      <)            �     <)            �      <)            �     8<)            �     �<)            U)     �<)            R     �<)            i     �<)            {     �<)            �      =)            �     =)            �      =)            �     0=)            �     @=)            �     H=)                 P=)            �     `=)            �     h=)            �     p=)                 �=)                 �=)            �	     �=)            %     �=)            :     �=)            P     �=)            `     �=)            q     #)        -         �%)        -         #)        �           #)        �         �$)        �         �%)        �         �%)        �         (#)        K          0#)        F          H#)        F          �$)        F          P#)                  X#)                  `#)                  h#)                  p#)                  x#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �&)                  �&)                  �&)                  �&)                  �#)        0          �#)        �          $)        [          $)        ?          $)        B           $)        �          ($)        v          0$)        �          8$)                  @$)        �          H$)        Z          P$)        f          X$)        �          `$)                  h$)        �          p$)        �          x$)        3          �$)        #          �$)        P          �$)                  �$)        �          �$)                  �$)        �          �$)        6          �$)        �          �$)        \          �$)        M           %)        �          %)        )          %)        �          %)        c           %)        |          (%)                  0%)        X          8%)        >          @%)        �          H%)        g          P%)        4          X%)        �          `%)        4          h%)        �          p%)        P          x%)        �          �%)        p          �%)        9          �%)        �          �%)        �          �%)        �          �%)        �          �&)        �          �%)        �          �%)        �          �%)        �          �%)        
           &)        &          &)        �          &)        :          &)        �           &)        f          (&)        �          0&)        �          8&)        �          @&)        z          H&)        �          P&)        k          X&)        A          `&)        .          h&)        �          �&)        X          �&)        X          �&)        	          �&)        �          �&)        �          �&)        l          �&)        2          �&)        �          �&)                   ')        G          ')        y          ')                  ')        ^           ')                  (')        \          0')        �          8')        �          @')        �          ())        �          0))        q          8))        �          @))        �          H))        �          P))        �          X))        �          `))        m          h))        �          p))                  x))        �          �))        d          �))        &           �))                  �))        �          �))                  �))        �          �))        �          �))        {          �))        �          �))        >           �))        C          �))        @           �))        �          �))        �          �))        �          �))        4           *)                  *)        f          *)        l          *)        U           *)        �          (*)                  0*)        �          8*)        g          @*)        �          H*)        h          P*)        H          X*)        �          `*)        �          h*)                   p*)                  x*)        W          �*)                  �*)        .          �*)                  �*)        >          �*)        �          �*)        �          �*)                   �*)        �          �*)        b          �*)        �          �*)        �          �*)        )          �*)        �           �*)        �          �*)        �          �*)        �           +)        �          +)        �          +)        G          +)        �           +)                  (+)        
          H,)        S          P,)        �          X,)        �          `,)        �          h,)        �          p,)        /          x,)        Z          �,)        �          �,)        �          �,)        ^          �,)        ^          �,)        �          �,)        V          �,)        -          �,)        �          �,)        �          �,)        �          �,)        /          �,)        �          �,)        �          �,)        �          �,)        �          �,)        �           -)        c          -)        �          -)        V          -)                   -)        �          (-)        n          0-)        �          8-)        �          @-)        �          H-)        �          P-)        �          X-)        �          `-)        _          h-)        �          p-)        �          x-)        i          �-)        �          �-)        �          �-)        �          �-)        �          �-)        '          �-)                  �-)        J          �-)        �          �-)        t          �-)        H          �-)        �          �-)        �          �-)        �          �-)        �          �-)        K          �-)        �           .)        h          .)        E          .)        �          .)        �           .)        �          (.)        `          0.)        I          8.)        �          @.)        �          H.)        �          P.)        H          X.)                  `.)        Z          h.)                  p.)                  x.)        �          �.)        n          �.)        �          �.)                  �.)                  �.)        �          �.)        
          �D)        N           �D)        �          �D)        �           E)        3          0M)        3          E)        �          8M)        �          E)        G          @M)        G          E)        �          HM)        �           E)        5           (E)        �           PM)        �           0E)        �          �L)        �          8E)        �          @E)        m          HE)        �          XN)        �          PE)        I          XE)        �          `E)                   hE)        k           pE)        @          �M)        @          xE)                  �M)                  �E)        �          �E)        C          �E)                   �E)        �           �E)        �          �E)                  �E)        E          �E)        �          �E)                  �E)        �           �E)        N          �E)        �           �E)        Q          �E)        L          �E)        �           �E)        �            F)        �           �M)        �           F)        2          F)        �          F)        �            F)                   (F)        Y          �L)        Y          0F)        �           �M)        �           8F)        9           �L)        9           @F)        �           HF)                  �M)                  PF)        *          �L)        *          XF)        y          �M)        y          `F)                  xM)                  hF)        �          �L)        �          pF)        �           xF)        @          `N)        @          �F)        K          �L)        K          �F)        �          �L)        �          �F)        d           �L)        d           �F)        (           �L)        (           �F)                  �F)        �           �F)        6           �F)        �          �M)        �          �F)        �          �F)        m           �F)        �          �F)        D           �F)        �           �F)        q          �F)                  �F)        �           G)                  G)                  G)        �           G)        V            G)        N          (G)        _           0G)        D          8G)        Q           @G)        *           HG)        G          PG)        �           XG)        	          `G)                  hG)                  pG)        a           xG)        I           �G)                  �G)                   �G)        �           �G)        9          �G)                   �G)        ;          �G)        U          �G)        7          �G)        Y           �G)        �           �M)        �           �G)        q           �G)        �          �M)        �          �G)        "          �M)        "          �G)        �           �G)        Z           �M)        Z           �G)        &           H)        f          H)        -           �M)        -           H)        a          H)        �          �L)        �           H)        =          (H)        �          0H)        k          8H)        �          @H)        !          HH)                  PH)                  XH)        ~           `H)        %          hH)        /          pH)        �           xH)        t          �H)        �           �H)        �          �H)        �           �H)        �          �H)                  �H)        �           �H)        `          �H)        0          �H)        �          �H)        P          �H)        �           �H)        ^           �H)        �          �H)        �           �H)                   �H)        7           I)        �           I)        9          I)        Q          I)                    I)        #           (I)        ^          0I)        �           8I)        c          @I)        �          HI)        [           PI)        d          XI)        �          `I)        �           hI)        �          pI)        �           xI)        w           �I)        �           �I)        �          �I)        J           �I)        �           �I)        "           �I)        �           �I)        ,          �I)                  �I)        0           �I)                   �I)        �           �I)                  �I)                   �I)        }          �I)        �          �I)        �            J)        ?          J)        U           J)        �           J)        �            J)        �          (J)        �          0J)        �          8J)        _          @J)        �           HJ)        !          PJ)        h           XJ)        �           `J)        �           hJ)                  pJ)        2           xJ)                  �J)        �           �J)                   �J)                  �J)                  �J)        �          �J)        �           �J)        ~          �J)        �          �J)        �           �J)        �          �J)                  �J)        T          �J)                  �J)                  �J)        O          �J)        �           K)        �          K)                   K)        L           K)                   K)        B          (K)        �           0K)        P          8K)        �          @K)        *          HK)        �           PK)        S          XK)        ]           `K)        \           hK)        �          pK)                   xK)        �          �K)        	          �K)        �          �K)        H          �K)        =          �K)        �           �K)        |          �K)        �           �K)        �          �K)                  �K)        }           �K)        �           �K)        R           �K)        
           @0)        �          H0)        $          P0)                   X0)        m          `0)                  h0)                  p0)        �          x0)        �          �0)        �          �0)        �          �0)                   �0)        �          �0)        �          �0)        c          �0)        +           �0)        t          �0)        1           �0)        I          �0)        4           �0)        �          �0)        8           �0)        �          �0)        ;           �0)        =            1)        �          1)        +          1)        �          1)        `           1)        C           (1)        `          01)        E           81)        G           @1)        H           H1)        �          P1)        �          X1)        �          `1)        e          h1)        M           p1)        O           x1)        P           �1)        S           �1)        �          �1)        �          �1)        �          �1)        �          �1)                  �1)        �          �1)        �          �1)        w          �1)                  �1)        `           �1)        �          �1)        	          �1)                  �1)        f           �1)        w           2)        l           2)        Q          2)        o           2)        �           2)                  (2)        t           02)        �          82)        �          @2)        v           H2)        y           P2)        �          X2)        �          `2)        �          h2)        �          p2)        �           x2)        F          �2)        �          �2)        �           �2)        �           �2)        %          �2)        �          �2)                  �2)        �           �2)        �           �2)        =          �2)        4          �2)        �          �2)        *          �2)        �           �2)        �          �2)        >          �2)        ]           3)        n          3)        �           3)                  3)        �           3)        �          (3)        �           03)        �           83)        �          @3)        �           H3)        �          P3)        R          X3)        �           `3)        �          h3)        �           p3)        �          x3)        �           �3)        O          �3)        �          �3)        �           �3)        /          �3)        �           �3)        �          �3)        �           �3)        �          �3)        �          �3)                  �3)        �           �3)        �          �3)        �           �3)        �          �3)        @          �3)        �           4)        �          4)        �           4)        �           4)        �            4)        �           (4)        X          04)        �           84)        �           @4)        D          H4)        �           P4)        3          X4)        d          `4)        �          h4)        �          p4)        �           x4)        J          �4)        �           �4)        D          �4)        �           �4)        �           �4)        v          �4)        �           �4)        �          �4)        y          �4)        �          �4)        �          �4)        Q          �4)        �          �4)        �          �4)        �          �4)        D          �4)        N           5)                  5)                  5)        /          5)        �           5)        ?          (5)        >          05)                  85)        �          @5)        #          H5)        �          P5)        $          X5)        %          `5)        �          h5)        t          p5)        )          x5)        �          �5)        -          �5)        �          �5)        �          �5)        /          �5)        �          �5)        5          �5)        8          �5)        <          �5)        k          �5)        �          �5)        8          �5)        8          �5)        R          �5)        �          �5)        X          �5)        [           6)        �          6)        O          6)        ~          6)        6           6)        W          (6)        8          06)        ,          86)        �          @6)        i          H6)        �          P6)        �          X6)        e          `6)        �          h6)        d          p6)        s          x6)        �          �6)        g          �6)        v          �6)        7          �6)        w          �6)        B          �6)        P          �6)        �          �6)        r          �6)        �          �6)        J          �6)        �          �6)        2          �6)        �          �6)        �          �6)        �          �6)        �           7)        q          7)        �          7)        �          7)        �           7)        �          (7)        �          07)        �          87)        �          @7)        �          H7)        �          P7)        �          X7)        c          `7)        �          h7)        �          p7)        �          x7)        �          �7)        �          �7)        �          �7)        �          �7)        �          �7)        G          �7)        1          �7)        S          �7)        �          �7)        	          �7)        �          �7)        �          �7)        �          �7)                  �7)        �          �7)        �          �7)        S           8)        �          8)        �          8)        t          8)        �           8)        �          (8)        �          08)        �          88)        u          @8)        �          H8)        _          P8)        �          X8)        �          `8)        �          h8)        $          p8)        m          x8)        �          �8)        �          �8)        �          �8)        �          �8)        �          �8)        �          �8)        N          �8)        �          �8)        C          �8)        �          �8)        Y          �8)        .          �8)        �          �8)                   �8)                  �8)        ,          �8)                   9)        `          9)                  9)                  9)        j           9)                  (9)        �          09)        
          89)        T          @9)        �          H9)        	          P9)        "          X9)        E          `9)        &          h9)        �          p9)        "          x9)        "          �9)        �          �9)        �          �9)        @          �9)        �          �9)        )          �9)        �          �9)        Q          �9)        ,          �9)        �          �9)        :          �9)        �          �9)        1          �9)                  �9)        �          �9)        4          �9)        5           :)        6          :)        8          :)        }          :)        >           :)        C          (:)        S          0:)        V          8:)        J          @:)        *          H:)        �          P:)        u          X:)        O          `:)        ;          h:)        F          p:)        U          x:)        X          �:)        Z          H��H��K' H��t��
  H���              �5�N' �%�N' @ �%�N' h    ������%�N' h   ������%�N' h   ������%�N' h   �����%�N' h   �����%�N' h   �����%�N' h   �����%�N' h   �p����%�N' h   �`����%�N' h	   �P����%�N' h
   �@����%�N' h   �0����%�N' h   � ����%zN' h
N' h   �0����%N' h   � ����%�M' h   �����%�M' h   � ����%�M' h   ������%�M' h    ������%�M' h!   ������%�M' h"   ������%�M' h#   �����%�M' h$   �����%�M' h%   �����%�M' h&   �����%�M' h'   �p����%�M' h(   �`����%�M' h)   �P����%�M' h*   �@����%�M' h+   �0����%�M' h,   � ����%zM' h-   �����%rM' h.   � ����%jM' h/   ������%bM' h0   ������%ZM' h1   ������%RM' h2   ������%JM' h3   �����%BM' h4   �����%:M' h5   �����%2M' h6   �����%*M' h7   �p����%"M' h8   �`����%M' h9   �P����%M' h:   �@����%
M' h;   �0����%M' h<   � ����%�L' h=   �����%�L' h>   � ����%�L' h?   ������%�L' h@   ������%�L' hA   ������%�L' hB   ������%�L' hC   �����%�L' hD   �����%�L' hE   �����%�L' hF   �����%�L' hG   �p����%�L' hH   �`����%�L' hI   �P����%�L' hJ   �@����%�L' hK   �0����%�L' hL   � ����%zL' hM   �����%rL' hN   � ����%jL' hO   ������%bL' hP   ������%ZL' hQ   ������%RL' hR   ������%JL' hS   �����%BL' hT   �����%:L' hU   �����%2L' hV   �����%*L' hW   �p����%"L' hX   �`����%L' hY   �P����%L' hZ   �@����%
L' h[   �0����%L' h\   � ����%�K' h]   �����%�K' h^   � ����%�K' h_   ������%�K' h`   ������%�K' ha   ������%�K' hb   ������%�K' hc   �����%�K' hd   �����%�K' he   �����%�K' hf   �����%�K' hg   �p����%�K' hh   �`����%�K' hi   �P����%�K' hj   �@����%�K' hk   �0����%�K' hl   � ����%zK' hm   �����%rK' hn   � ����%jK' ho   ������%bK' hp   ������%ZK' hq   ������%RK' hr   ������%JK' hs   �����%BK' ht   �����%:K' hu   �����%2K' hv   �����%*K' hw   �p����%"K' hx   �`����%K' hy   �P����%K' hz   �@����%
K' h{   �0����%K' h|   � ����%�J' h}   �����%�J' h~   � ����%�J' h   ������%�J' h�   ������%�J' h�   ������%�J' h�   ������%�J' h�   �����%�J' h�   �����%�J' h�   �����%�J' h�   �����%�J' h�   �p����%�J' h�   �`����%�J' h�   �P����%�J' h�   �@����%�J' h�   �0����%�J' h�   � ����%zJ' h�   �����%rJ' h�   � ����%jJ' h�   ������%bJ' h�   ������%ZJ' h�   ������%RJ' h�   ������%JJ' h�   �����%BJ' h�   �����%:J' h�   �����%2J' h�   �����%*J' h�   �p����%"J' h�   �`����%J' h�   �P����%J' h�   �@����%
J' h�   �0����%J' h�   � ����%�I' h�   �����%�I' h�   � ����%�I' h�   ������%�I' h�   ������%�I' h�   ������%�I' h�   ������%�I' h�   �����%�I' h�   �����%�I' h�   �����%�I' h�   �����%�I' h�   �p����%�I' h�   �`����%�I' h�   �P����%�I' h�   �@����%�I' h�   �0����%�I' h�   � ����%zI' h�   �����%rI' h�   � ����%jI' h�   ������%bI' h�   ������%ZI' h�   ������%RI' h�   ������%JI' h�   �����%BI' h�   �����%:I' h�   �����%2I' h�   �����%*I' h�   �p����%"I' h�   �`����%I' h�   �P����%I' h�   �@����%
I' h�   �0����%I' h�   � ����%�H' h�   �����%�H' h�   � ����%�H' h�   ������%�H' h�   ������%�H' h�   ������%�H' h�   ������%�H' h�   �����%�H' h�   �����%�H' h�   �����%�H' h�   �����%�H' h�   �p����%�H' h�   �`����%�H' h�   �P����%�H' h�   �@����%�H' h�   �0����%�H' h�   � ����%zH' h�   �����%rH' h�   � ����%jH' h�   ������%bH' h�   ������%ZH' h�   ������%RH' h�   ������%JH' h�   �����%BH' h�   �����%:H' h�   �����%2H' h�   �����%*H' h�   �p����%"H' h�   �`����%H' h�   �P����%H' h�   �@����%
H' h�   �0����%H' h�   � ����%�G' h�   �����%�G' h�   � ����%�G' h�   ������%�G' h�   ������%�G' h�   ������%�G' h�   ������%�G' h�   �����%�G' h�   �����%�G' h�   �����%�G' h�   �����%�G' h�   �p����%�G' h�   �`����%�G' h�   �P����%�G' h�   �@����%�G' h�   �0����%�G' h�   � ����%zG' h�   �����%rG' h�   � ����%jG' h�   ������%bG' h�   ������%ZG' h�   ������%RG' h�   ������%JG' h�   �����%BG' h�   �����%:G' h�   �����%2G' h�   �����%*G' h�   �p����%"G' h�   �`����%G' h�   �P����%G' h�   �@����%
G' h�   �0����%G' h�   � ����%�F' h�   �����%�F' h�   � ����%�F' h�   ������%�F' h   ������%�F' h  ������%�F' h  ������%�F' h  �����%�F' h  �����%�F' h  �����%�F' h  �����%�F' h  �p����%�F' h  �`����%�F' h	  �P����%�F' h
  �@����%�F' h  �0����%�F' h  � ����%zF' h
F' h  �0����%F' h  � ����%�E' h  �����%�E' h  � ����%�E' h  ������%�E' h   ������%�E' h!  ������%�E' h"  ������%�E' h#  �����%�E' h$  �����%�E' h%  �����%�E' h&  �����%�E' h'  �p����%�E' h(  �`����%�E' h)  �P����%�E' h*  �@����%�E' h+  �0����%�E' h,  � ����%zE' h-  �����%rE' h.  � ����%jE' h/  ������%bE' h0  ������%ZE' h1  ������%RE' h2  ������%JE' h3  �����%BE' h4  �����%:E' h5  �����%2E' h6  �����%*E' h7  �p����%"E' h8  �`����%E' h9  �P����%E' h:  �@����%
E' h;  �0����%E' h<  � ����%�D' h=  �����%�D' h>  � ����%�D' h?  ������%�D' h@  ������%�D' hA  ������%�D' hB  ������%�D' hC  �����%�D' hD  �����%�D' hE  �����%�D' hF  �����%�D' hG  �p����%�D' hH  �`����%�D' hI  �P����%�D' hJ  �@����%�D' hK  �0����%�D' hL  � ����%zD' hM  ����PH�Hx�w ���=���ZÐPH�Hx�w ���'���ZÐH�
��@ (�(��X��Y��\��c����(�(��"���D  (�������     �\��Y��^��X��
�f.�     �\��Y��^��X��'�����    �\��Y��^��X��������    �X@H ���� �X0H �%��� �X% H ���� (��&����     (������     (��0���(�(��X��F���f�     ��A��A���*�AT�A*�M���A*�UH��SL��H��0�
�    H��@��t
1�H��f�G���t� H��  �   @����  @����  @���
  ��1���@���H�t
�    H��@��t
1�H��f�G���t� H��  �   @����  @����  @���  ��1���@���H�t
�    H��@��tE1�H��fD�W���t� H��  �   @����  @����  @���   ��1���@���H�t
�    H��@��tE1�H��fD�G���t� H��  �   @����  @����  @����  ��1���@���H�t
�    H��@��t
1�H��f�G���t� �B    �B    H�    ǂ      ǂ      �fD  H�z�B f��@���
���f.�     1�H����f�G�@�������f�     �    ��H�������fD  H��  Ƃ   f��@������@ 1�H����f�G�@���������    ��H�������fD  H��  Ƃ   f��@������@ E1�H����fD�_�@�������    �    ��H�������fD  H��  Ƃ   f��@������@ E1�H����fD�O�@�������    �    ��H�������fD  H��  Ƃ   f��@��� ���@ 1�H����f�O�@��������    ��H�������@ f.�     SH��H�?H��t����H�    �C    �C    ǃ      [�@ f.�     AWAVAUATUSH��H��  ��H��$p  ��$x  ��$|  ��  Hc�E1�H��1�E1�1��Df.�     E���  A9�t
  ����  ���[���  ������  ��  ��  ��  ���[��  ��  ��  ��  ��  �[��  �   ��  �   ��  ��  �    E1�)���H��   ���<�    L�,L��   L��   L��   L��   1���  A���� A)D ��
  �� AAD��  �� AAD��  �� AADfn�  fp� fo�fr�f��fr��AH��A9��s����)�9��  Hc���  H���@��
  ��  ��  ��  ��  ��  ��  �����������  �F��   H���  H�����@��
  ��  ��  ��  ��  ��  ��  �����������  tYHc���  H���@��
  ��  ��  ��  ��  ��  ��  ���������  H������H��H������H������@ �D$�D$ �]���H�
A��$l  ��H��' H�H��tA�t$0���  ��A�t$4���  �H�m' H�8 tA�|$8 �&  A�|$9 �  H��' H�8 tH�{' H� H��tA�|$<��H�U' H�8 tA�|$@ ��  A��$L   tA��$M   ��  H�:' H� H��t
A��$P  ��H��' H� H��tA��$X  A��$T  ��H��' H� H��t"A��$h  A��$d  A��$`  A��$\  ��H�' A�t$ �  �A�t$$�  �H��' A�T$� "  � #  �H��
' �H�E H���   []A\A]A^A_��f�     H��
' �   �H�-�' ����H�
' �  �H�' �D  �A���J  A���  H�9' �D  H�D$�H�^' H�D$H�D$��
' H�D$�����H�
' ������H�&
' ������L�-x	' H�	' I�}  �  L�<$H��H�D$���  I��D  �I��H  ��  �H�D$0�D$0   �q�  H�D$H����D$0���T  �����  H�$H�R' E1�H�-�	' L�`A@ A����  I��A��A�U ��
�& ƅL   H�H���   []�@ H���& �D$H�2H���& H�:�;����D$H��[]�H��� uQ�G���$u4臵�����   uH�O�& H�0H���& H�8�����1҉�H���@ H�� ' H��   ��H�
�BH��H)�H��I��I��H��~D  H�T��H�T��H��L9�u���FH��[]A\A]Ð1��H�H)�H��H����   H��A�   H)�H��L��H�T$H�4$訫��H�4$H�T$H�HI��I��H��tH�H�H�H9���   H��L���    H��tL�L�H��H��H9�u�H��H��H)�H��H��I�l�L�KL9�t1H��H�� H��tL�L�H��H��I9�u�H��I)�I��J�l�H�;H��t�ۛ��L�#M�H�kL�c�����H�< H9�vH��I������H)�H������H���I��������L9�w�H��I��H)�I��H��H��������   E1������f.�     @ AVAUATUH��SH��H��H�GH;GtTH��tG�H��H�H�GH�C�H�W�H)�H��H��tH��H��H)�������] H��[]A\A]A^�D  1��@ H�H)�H��H����   H�4 H9���   I��I������I)�I��L��H�T$�ҩ��H�T$I��K��H��t��H�3H��E1�H)�H��H��tL�$�    L��L���e���H�KO�D&E1�H)�H��H��tL�$�    L��H��L���7���I��H�;M�H��t�4���M�L�3L�cL�k�����I��A�   I)�I���L���H��������?H9��(���I��E1�L�,�    I)�I��H���0�������D  H���D  AUATUSH��H��(L�GH�GL)�H��H9��  L��I��H)��	H���AH9��  H��M��I)�M9��0  L��L��f.�     H��tH�9H�8H��H��I9�u�H�CL��H�H)�H�C1�H��H��H��H��~fD  I�L��I�L��H��H9�u�H�H9�txH�FH��H)�H��H��H��H��H��I��M�t5H��v/(�1���I��H��I��H9�BBTw�I9�N��t I�A�A	�AAH9�t�AI�AAH��([]A\A]�ÐH)��/  H��L���H��t	��@H��H��u�H�CH��I9�H�C�  H��H��tH�
H�H��H��I9�u�H�FH��H{L��H)�H��H��H��H��H��H��H�t9H��v3(�1���I��H��I��H9�BBTr�H9�L���7���I�A�A	�AAI9����������    H�H��������I��I)�I��M)�L9��Z  I9�H��I��IC�I)�I��I��   H������H��H�L$H�T$H�t$�b���H�t$H�T$I��H�L$O��I��f�     M��tH�I� I��I��u�H�H9���   I��L��f�     H��tM�L�	I��H��L9�u�H��H��H)�H��H��I�D�L�,�H�CH9�t5H��L���    H��tL�L�H��H��H9�u�H��H)�H��M�l�H�;H��t�s���L�L�#L�kH�k�����I9������M��u;1�E1�����L�������L�������H��H�C����L���a���H�=� 藧��J�,�    ����f.�     �GÐf.�     SH��H���G���$tH���& H��   ��C    H�C    H��[��     �D$A�D���   Љ��   H�H���   ��@ f.�     H�Ǉ�       E1�Ǉ�       1�1�1�H���   ���fD  AVAUATUS� H����  ��A����  ��A����  H�-n�& �G���   ���   H�wHǇ�       Ǉ�       ��  �U H�?�& A�M�A�T$�1�1��H�s$�C$    ���  �U L�5>�& 1�A�H���& H�s�!  �C  �?�H�	�& �!� �L�%8�& �   A�$L�-H�& �C(�   A�U �D  A�$�D  �C)A�U �q  A�$�q  �C*A�U ��  A�$�C+H�W�& ��  ��  A�$�  �C4A�U H�s8�  �U H�s,��  �U H�s0��  �U H�(�& �  �  �H�s �C     �i�  �U H���& 1���
���H�
f��I�O8I�w@�T$H�����H�T$ H��$�   H����e�������H�T$ H��$�   �D$H��e��I�O8I�w@�D$H����H�T$ H��$�   �D$�L$H�e��I�O8I�w@�D$�L$H����H�T$ H��$�   �D$L�L$�T$H�We��I�O8I�w@�D$L�L$�T$H����H�T$ H��$�   �L$�T$H�e��I�O8I�w@�L$�T$H�#���1��i���H�T$ H��$�   H���Un��I�OhI�wpH9��T���H�T$ H��$�   �2n��I�OhI�wpH9��X���H�T$ H��$�   �n��I�OhI�wpH9��\���H�T$ H��$�   ��m��I�OhI�wpH9��`���H�T$ H��$�   ��m��I�OhI�wpH9��d���H�T$ H��$�   H���m���d���H���& D�L$HH��$�   �   ��D$H�q���H�
�BH��H)�H��I��I��H��~D  H�T��H�T��H��L9�u���FH��[]A\A]Ð1��H�H)�H��H����   H��A�   H)�H��L��H�T$H�4$�p��H�4$H�T$H�HI��I��H��tH�H�H�H9���   H��L���    H��tL�L�H��H��H9�u�H��H��H)�H��H��I�l�L�KL9�t1H��H�� H��tL�L�H��H��I9�u�H��I)�I��J�l�H�;H��t�;`��L�#M�H�kL�c�����H�< H9�vH��I������H)�H������H���I��������L9�w�H��I��H)�I��H��H��������   E1������f.�     @ H���D  AUATUSH��H��(L�GH�GL)�H��H9��  L��I��H)��	H���AH9��  H��M��I)�M9��0  L��L��f.�     H��tH�9H�8H��H��I9�u�H�CL��H�H)�H�C1�H��H��H��H��~fD  I�L��I�L��H��H9�u�H�H9�txH�FH��H)�H��H��H��H��H��I��M�t5H��v/(�1���I��H��I��H9�BBTw�I9�N��t I�A�A	�AAH9�t�AI�AAH��([]A\A]�ÐH)��/  H��L���H��t	��@H��H��u�H�CH��I9�H�C�  H��H��tH�
H�H��H��I9�u�H�FH��H{L��H)�H��H��H��H��H��H��H�t9H��v3(�1���I��H��I��H9�BBTr�H9�L���7���I�A�A	�AAI9����������    H�H��������I��I)�I��M)�L9��Z  I9�H��I��IC�I)�I��I��   H������H��H�L$H�T$H�t$�Rl��H�t$H�T$I��H�L$O��I��f�     M��tH�I� I��I��u�H�H9���   I��L��f�     H��tM�L�	I��H��L9�u�H��H��H)�H��H��I�D�L�,�H�CH9�t5H��L���    H��tL�L�H��H��H9�u�H��H)�H��M�l�H�;H��t�c\��L�L�#L�kH�k�����I9������M��u;1�E1�����L�������L�������H��H�C����L���a���H�=�h �m��J�,�    ����f.�     �8
H��t��� �l fW�f(���fD  �Gx�*��Gy�*��Gz�*��fD  �Wx�O|���   Z�Z�Z�� �Wx���   ���   �n���fD  �Gx�*��Gz�*��G|�*��K��� �Gx�*��Gz�*��G|�*��+��� �*Wx�*O|�*��   ������Gx�H*ЋG|�H*ȋ��   �H*�������k �8� �
X�����   uH����[]��     H�5| H����W�����   t�H�5| H����W�����   t�H�5L{ H���W�����	   t�H�5<{ H���W����u�   �H�5+{ �E  H���{W�����   �m���H�5f� H���_W�����   �Q���H�5�z H���CW����t�H�5�z H���0W����Ƀ��"����f�AUI��ATI��U�   SH��H�5�z H��H�����V����uH����[]A\A]�D  H�5�z H����V����t�H�5�z H��@��V����t�H�5wz H��@�
�  ��f�u��C|�����Cx    ǃ�      ƃ�   �ƃ�    �sX����t��    �Ca[�f���t|�� �/tt��
   �����H�5v\ H���A7�����   �����H�5f\ H���%7�����   �����H�5T\ H���	7�����   �{���H�5�Z �E  H����6�����   �[���H�5�q H����6�����   �?���H�5\ H���6����Ƀ��#���@ Ƈ�   ��     AUATUSH��H���]	  H�R ��H��H��Hc�H���D  �   H��   �.6��fD  H��H��[]A\A]� �   H��   �6����@ �   H��   ��5����@ H��tC�; t>H�5W[ H����5�����
  �E0H��   �65���@ H��t�; �n	  f�H��& H�0H�& 1�H�8�eD��H��H��[]A\A]��     H��tˀ; t�H�5Z H���w5������  H�5tA H���`5������  H�5n H���I5����tH�55\ H���65������  H��~& �   L�(I���   L�g�M�������H�u �=���������I���   1�L��1���;������f�H�I~& H�ھ   H�8�9>�����z���f�H��������; �����H�5=Y H���4������  H�5�@ H���4������  H�5.m H���q4����tH�5][ H���^4�����&  �Er �   � ����     H���w����; �n���H�5�X H���4�����(  H�5@ H���4�����  H�5�l H����3����tH�5�Z H����3������  �Eq �   �����     H��������; �����H�5=X H���3������  H�5�? H���3������  H�5.l H���q3����tH�5]Z H���^3�����&  �Ep �   � ����     H���w����; �n���H�L$L�L$0L�D$ H�5oX H��1�H���>�����},��  ����  �<$���   ��  �t$���   ��  �T$ ���   ��  �L$0���   ��  �B���E,H��   �2���U���D  H��������; �����H���6��H�}H��H���_<������f.�     H��������; @ �����H���R6��H�}��@ H���o����; �f���H�5�V H���2�����  H�5> H��� 2������  H�5�j H����1����tH�5�X H����1������  �Es �   �x���H�){& H�ھ   H�8�;�����Z���f�H��������; �����H�5V H���1�����c  H�5|= H���h1�����L  H�5j H���Q1����tH�5=X H���>1�����  H��z& �   L�(I���   L�g�M�������H�u �9���������I���   1�L��1���7������f.�     H�Iz& H�ھ   H�8�9:�����z���f�H�)z& H�ھ   H�8�:�����Z���f�H��������; �����H�T$0H�5�U 1�H���(<�����?  �D$0.�Z �,  �EP�   ����D  H�������; �v���H�L$0H�T$ H�5�U 1�H����;������  �D$ ����  �T$0����  �E�U H���L/���   ����f�H�������; ����H�5�f H���/�����  H�5"U H���/�����  H�5�f H���/�����Y  ǅ<     �   �-���D  H��������; �����H�L$0H�T$ H�5�T 1�H����:�����
  �D$ ����  �T$0����  �E$�U(H���t.���   �����f.�     H�ix& H�ھ   H�8�Y8��������f�H�Ix& H�ھ   H�8�98�����z���f�H�)x& H�ھ   H�8�8�����Z���f�H��������; �����H�5�S H���.������  H�T$0H�5U 1�H���:�����(  �D$0���  �E@H��   �-��������    H���g����; @ �Z���H�5�R H���.���������H�5: H����-���������H�5�f H����-����tH�5�T H����-������   �   H��   �-���c��� H��������; �����H�5%R H���-�����G���H�5�9 H���p-�����0���H�5f H���Y-�����1���H�5AT H���B-��������f.�     H�iy& �k���@ H��u& �[���H�}v& H��H�H���   �=��L�#I��$�   H�_�H��t!H�u �5����uI��$�   1�H��1���3��H��   �/���l���H�v& H��H�H���   �+=��L�#I��$�   H�_�H��t!H�u �.5����uI��$�   1�H��1��f3��H��   �y6������H�T$0H�5�R 1�H���D$0�   ��7����������D$0=�   ������U,������� 	��Y����Es�   �����Ep�   �����Eq�   �����Er�   ����H�51Q H����+����������E0 ������E@M���H��   ��*���R���ǅ<      �   �>���ǅ<     �   �*����4$���   �3����T$���   �#����L$ ���   �������;������fD  AUATUSH��H��t& H�|$H�H��t6H��  H��t*H�o`D�l$L�ghH��u$H��t�9��H�Hǂ      H��[]A\A]ÐM��t׀}` uы}8�M<����uH�H��  �D  �I*�H�E H���P`L��AƄ$)  ��)��H�H��  �f�f.�     AVAUATUSH��H��   �G,��� H�t$pH�|$`�����H*�������*������*�H��$�   �*��Y��Y��Y��t$�Y��D$0�L$@�T$P�2��D�s0A���Q  �5)T W��D$ǃ�   ���f�Y��=T �%�� ǃ   333fǃ  ����ǃ   ����t$ǃ  ����ǃ  �����|$�X�ǃ  ����� ǃ   ����ǃ  �����Y�.�s.�� ����  �=^S ��  1��D$�Y��|$ �Y�.�s.F� �   ���  �5'S ��$  ���� �L$ǃ<  �����Y��=S ǃ@     �ǃD  �����t$$�|$�X��Y�.�s.
  �Y�	�1�.�s.
  �Y�	�.���
  .-ŉ ��   s�H,�	�A��L����L$p��  ��\L$$��(  ��A��H�����4  L������$�   %   @�D$`��8  �d$,�\$(�,���D$1��XLN �d$,�\$(�l$P�L$@�T$0�Y�.�s.� �   ��W  �Y�1�.�s.�� �  � �*  �Y�	�1�.�s.
  �D$<E��4  �K�A�t$L��(I�E �$    A���P8H�D$ �xP ������t$t$L�U�|$I�E E��4  �$    D�G���L���P8�l$AnHL;|$0�����M��A���  A9��  �T  E�gE��  L��A�o A���  A���  E���   A�D$�A��  ���D$,I�G8D�E���  Ή��v�����D�A����� A)؉D$ ��A��   ���D$0��D$A�D$�ƉD$(I�E �PHA��)   uA�\ �\  D�t$ A���  A���  E��@  D��D�A���WL��ƉD$<I�E ���$    �P8A���  E��@  D��t$(L��PI�E �$    A���P8D��+D$,������������A  )�A�L$D�d$(�}�l$�L$@���|$8�|$ A��)ǉ|$ M�U E��@  ���$    A��D��D��L��A�R8A��"   I�U E��<  L�Z8tA����� A��   OA�F�E�T$��U�L���$    ��D�։D$A��D�T$A��M�] �SE��@  D���$    D��A��L��A�S8A��#   I�E D�T$E��<  H�@8tA����� A��   OD�t$A�؉��$    D��L��A������D����D;t$�	���D�t$ �D$,L��l$E��<  E���  D��É����I�E �$    ��D�c�D��D���P8I�E E��<  ��E���  ��މl$L���$    �P8I�E �kE��@  E���  �T$L�����$    �P8I�E E��<  D��A���  D��D�D$8L���$    �P8I�E D�d$8��E��<  A���  ��L���$    E���P8I�E E��@  E��A���  ���L���$    �P8A���  A���  D��I�E A��!   E��L  ED�H  ���WD�F�L��t$(�PHD�t$0I�E L��E��<  E���  A���  �$    D��D���P8�\$@I�E L��E��<  E���  A���  �$    �ى��P8A���  I�E ��E��<  �L$<L���$    A���P8A���  I�E D��E��<  ���$    L��A���P8A��)   �z  A�\ �o  A�p ��  I�G8�   A�wA�O$��  �B���M�A�r ��  A��$   I�E H�@H��
  E��L  ���A� �,QD�d �W�uD�D;L��A�L$�E�t$��A�G E��<  ��D��L��PI�E �$    A���P8A�G E��<  D��D��L��PI�E �$    D��P8A�G E��<  ��D��L��TI�E �$    A���P8A�G E��<  ���L��D�@I�E �$    A��P8A�G E��@  D��D��L��PI�E �$    D��P8A�G E��@  ��D��L��TI�E �$    A���P8�غVUUUA�O ��E1�M�] �E��4  �$   �ǉ�D�D������D$A�։T$A)ƃ�B�T1A��D�1���D�T$D)҃�@�ƍt5���L��A�S8�D$+D$L��A�O D�T$M�] E��4  �$   A�TA�4D�D��D)�D�A�S8H��U& H� ���    �9  I�G8A�w�  A��%   I�E �nL�XH�  E��H  A����� A��   OA�G D�d ��L��A��A�L$�A���PD�DA��A�G E��<  D���L��PI�E �$    A���P8A�G E��<  D��D��L��PI�E �$    D��P8A�G E��<  ��D��L��A���TI�E �$    A���P8A�G E��<  ���L��D�@I�E �$    A��P8A�G E��@  D��D��L��PI�E �$    D��P8A�G E��@  ��D��L��A��A���TI�E �$    D�A���P8����*��A�VUUU��E��4  L��Љ�D)��ƉD$��A��A�G A�҉T$E)�A�TD��D�T$)ȍN�I�E A���$    ���P8A�W D�\$I�E E��4  D�T$D�DE)�H�@8E����G  A�T�$    A�����L����I�E A�W E��4  H�@8A�T�$    A�ЋL$��L����H��X[]A\A]A^A_�f.�     H�D$ H�xh �   �\$(\$����� l$@M�] �s��E��@  L��A����D$H�$    �UA�S8D�d$Dd$,�K�D$HM�] L��E��@  �$    A��A�t$��A�S8D�D$���� I�E E��<  A��ى�D��L���$    �P8I�E �|$��E��<  ����$    D�GL���P8�|$I�E D��E��<  ���$    A����L���P8I�E E��<  A���$    D��T$D��L���P8�S����     �xp��  �3  I�V8�\$(\$I�E A�NA��  ��  A�VH�s�D�D���L���PH�����    E��p  E��� ���A�vI�V8A���  I�E D��  �W&��$A��  A��A���L���PH�����    E��L  ����@ A�T�$    ���L����I�E A�W E��4  H�@8A�T����fD  A�G AG(L��I�W8A�wM�] E��<  ��+�  ��D�@��$    �ʉ��A�S8A�G AG(L��I�W8A�wM�] E��@  ��+�  ��D�@��$    �ʉ��A�S8I�O8A�G AG(A�wE��<  ��  �P�I�E �$    �����A��L����P8I�O8A�G AG(A�wE��@  ��  �P�I�E �$    �����A��L����P8A�WAW$L��A�G AG(M�] E��<  �r�I�W8��D�@�+�  �$    �ʉ��A�S8A�WAW$L��A�G AG(M�] E��@  �r�I�W8��D�@�+�  �$    �ʉ��A�S8A�wAw$I�O8A�G AG(E��<  ��+�  �P�I�E �$    ��A��L������P8A�wAw$I�O8A�G AG(E��@  ��+�  ���P�I�E �$    ��A��L����P8I�W8A�G L��A�wM�] E��<  ��  D�@�$    ����ʉ��A�S8I�W8A�G L��A�wM�] E��@  ��  D�@�$    ����ʉ��A�S8I�O8A�G A�wE��<  ��  �PI�E �$    A�������L����P8I�O8A�G A�wE��@  ��  �PI�E �$    A�������L����P8A�wAw$I�O8A�G E��<  ��+�  �PI�E ���$    A�Љ�L����P8A�wAw$I�O8A�G E��@  ��+�  �PI�E �$    A�Љ�L������P8I�W8A�GAG$A�O E��<  ��  D�A�p�I�E �$    ω���L����P8I�W8A�GAG$A�O E��@  ��  D�A�p�I�E �$    ω���L����P8�x���D  A���  A���  �L$ I�E E��H  �VD�G�A�t$��L���PH����f�     E��H  A����� A��   O�a���fD  E��$  �����A�D$)݉D$@�E�D$8�����H�
( ��' �c���A��A�������f�H�    �G( �G) �GX   Ð�     H���  H��t	H�H�@X��H�
 �V����A�$�  A��$|  �    A��$�  A��$�  �X�f�fZ��\
 f�fZ�������A�$�  A��$�  �A�$�  A��$|  A��$�  �    �X�f��xfZ��\
 f�fZ������A�$�  A��$�  �A�$�  A��$|  A��$�  �    �X�f��pfZ��\
&  H�W�{  ��������<���L���!����/���@ 1��!�������� ���   �  �F�=�   �����H�߉t$��������t$�v�����  ���h���H��   ;B��X���;�  tH�߉t$�4����t$L�l$0H�|$@@�־   L�������H�T$@Hc�  H��   H�J��)�����  ����  ��  H�D$@H�x�H;=d!& �����H�=�&  H�W�  ������������L������������Ǉ      �����Ǉ      ����fD  ��߃�C�������V������n���fD  H��   ��  H�@�Q9�L�  �D���@ ��  1���  ��I�  ��  ������  9�  tH�������L�d$@L�l$0H�5�
����P��J��H����|����P��J��H����x���H��H�D$@L��H�x�����H���������H���H��H�D$0L��H�x������m���H�=�� �����@ AUATUSH��H��  H���h  H���8���  �� �����
  ����  ��   ����  ��  ����  �^  ����  ��   H�u@H�UPH����  H��A�ă���  ��  ���"  ��  ��	��"  ��  1�E��f�L$ �@
  �f�D$ �} ����  H��$�   H�5l� ��1�H�������H��
H����������!�%����t�f�     ��H�������  D�H�JHD� �H��H)��  f.�     H�5�
 1�H������H�Ĩ  []A\A]Ð�Ex-   Hc�x�H��& H�H��  H��   H)�H��i��m۶9�~�H��H��H��H)�H�,H�E H�x� ��  L�-�& L�d$ L��I�EH�D$ H�E H�p�H�������{   �   L������H��L������H��$�   L��H���@����}   �   H�������H��H���S���H��$�   H�x�L9��^
  H�D$ H�x�L9������H�=�&  H�W��
  ������������H������������fD  ����  ��   ���`  ��  E��Ǆ$�       ��  ���$�   ����   H�mxH����  H��� ���H��H��H�������H�Ĩ  []A\A]�@ H�5�� �	   H��������8����    ��
H����������!�%����t����� E���D$  �  ��D$ ����  �}| ��H��$�   ���w  H�5�� H��1��k���H��
H����������!�%����t�����@ E���D$     �n  ��T$ ���    H��$�   ��  H�5�� H��1�����H��
H����������!�%����t��L���@ 1�E��f�D$ �9  �f�D$ �} ��H��$�   ��  H�5P� H��1�����H��
H����������!�%����t������ E���D$  �  ��D$ �}| ��H��$�   �  H�5�� H��1��M���H��
H����������!�%����t�����fD  1�E��f��$�   ��  �f��$�   f�����������@ E���D$     �x  ��T$ ���    H��$�   �g  H�5h� H��1�����H��
H����������!�%����t�������g�����D  �3  E���D$     ��  H�E@��T$ H�& H��E8-    H�H�4�    H��H)�H�P  H�pH�@H���W  H����    H��H�@H���-  ;P v�H�@��D  E���D$    �v  ��D$���   ����  L�d$ H�5� ��1�H��$�   L�������D$L��H��   Z�����H��
H����������!�%����t������D  E��H�D$    ��  ��D$���   ���9  L�d$ H�5�� ��H��$�   1�L���4����D$L��H��   ����H��
H����������!�%����t��_����    H�����������H9�t	;Q H�q(s�H��$�   H�5g� 1�H�������H��H��������U���H�uXH��$�   ����$�   �r���H��$�   H�5}� 1�H����������H�5g� H��1��j����Z���H�5�� H��1��T�������H�5�� H��1��>��������H��$�   H�5� 1�H��� ����X���H�5�� H��1��
����[���H�5�� H��1�����������   H�5�� ����H�uXH�|$ ���D$ �����H�uXH�|$ �ҋT$ ����H�5:� �   H�������H��  �7���H�uXH�|$ ���D$ �����H�uXH�|$������H�uXH��$�   �ҋ�$�   �j����}8���������  E����  L�-�& D�e8I�E A�����A�L$H���  H+��  9�|A�t$ H���  1�Hc�����I�E Mc�H�uXL���  L���UPC�D%  M���@  L��H��������e���H�uXH�|$���P���H�uXH�|$ ���D$ ����H�uXH�|$ ���D$ ����H�uXH��$�   ����$�   �e���H�uXH�|$ �ҋT$ �z���H�uXH�|$ ���D$ ������D$H��$�   H�5�� �   Z�H���!����x���H��$�   �D$H�5�� �   H������������H�5;� �   H�������}���H�uXH�|$ �UP�T$ �X����E8=��  uFE��HǄ$�       udH�E@H�0H��$�   H�������H�5�� H�������!���L�m@������@ t�H�E H���P��u�H�5� H���a����E`�����H�uXH��$�   �UPH��$�   �H�
  �: �  H�5�� H���ڽ����H�L$t�H�5`� L���½����H�L$�m���H�5�� L��H�L$衽����H�L$tH�5F� L��艽����H�L$��  A�|$P �[����   �6���fD  H���D$0    �?  H�T$0H�5�� 1�L������������  �|$0����   �  H��& H�H��  H��   H)�H��i��m۶9���  Hc�H��H��H��H)�H�L(I�L$XH��  H�D0A�|$pI�D$`����f�     H��H�L$H�D$0    ��  H�T$0H�5� 1�L���3�������H�L$�c  H�D$0H��  I�L$(I�D$h�(����     1�H��& H9GX�
����u���D  1�H���e����: f��Z���H��& H9GX�I���H�ohH�L$H���7���H�5�� H���X�����H�L$�   �} ������EH��蓹�������fD  1�H��& H9GX�����H�ohH�������H�������H�5�� H����������  H�5y� L���ٹ����������E �X����     1�H��& H9GX�}���H�ohH���p���H���g���H�L$ H�T$L�D$0H�5� 1�L���'������d  H�5� L���_�����tH�5	� L���L���������H�Ep    H�Eh    �   H�E`    �����f�1�H�O& H9GX�����H�h �����H�������H�L$ H�T$L�D$0H�5�� 1�L��������������D$I�|$h�   ��t$0�L$ ��~=�   �  ������   �1���~A��f� �A�����   AN�	Ѕ���  ��   ���   N�	��   �Ox�-���D  1�H�� & H9GX����L�whM������H�������H�5�� H��� ������p  H�5�� L���	������Y  H�5�� L���������  H�5�� L���۷�����  H�5�� L���ķ������  H�5t� L��護�����  H�5a� L��薷������  H�5I� L����������  H�55� L���h������0���W���� (у��g  1�������H��   H�@H���AT|H��IT$h���   ID$h���   �����fD  1�H�/�% H9GX�����H�ohH�L$H�������H�������H�5�� H���ö����H�L$�!  H�5� L��觶����H�L$�  H�5)� L��苶����H�L$��   H�5n� L��H�L$�j�����H�L$t4H�5� L���R�����H�L$tH�5�� L���:�����H�L$������EY H�ϻ   �|���������    H��@1ۉ�[]A\A]A^��    �����������@ H�	 & H�0H�G�% 1�H�8���������     A�D$PH�ϻ   �
L�H���% H�t$ H��@  H��H  H)�H������  �r�H�D$0    H�t$PH��$�   H�t$pH��$�   H�t$XH�t$ H��  H��$�   H��$�   H�t$xH��$�   H�t$h L�l$0E��I��J�<(H����H�D$ �f  �xT H��@  J�<(�)  H������  L�|$ H�t$hI��@  J�(H�H���PPA�T �  �{8��  �   ��  �{a �$ �D$( �
  H�H���P����  A�   �H�|$ H���������D$gu�{8��  �D$gH��$�   H�T$pH�5�� H�|$XL�`�H�D$D�d$����H�\$8H�{H;{�E  H���V  H�t$X輐��H�CH�t$8H��H�FH��$�   H�x�H;=��% ��  @���	  �<$ t�|$( ��  �|$, ��  �<$ H�L$@H�q�i  �|$( ��  �|$g ��  H;q��	  H���  H�D$ ��  �H�AH�t$@H��H�FE����  H�D$HǄ$�       H�pH;p�z  H����  �    H�@H�t$HH��H�FH�D$8H�H�@H)�H��L�|���D$`�D$(H�D$ D9�   �R  �D$1�1�E1���E��D$,��   �   f.�     H�D$D�,(D�,$I�H�P�L�bL;`�w�H���~L��L���_���I�H�P��$�I�H�P�H;C�% ��  H�D$F��  ��t$��uG�H��9l$~;��t��$.A�.   � 9l$,~�H�D$1ۋ��  A��9D$(��H��9l$�H�D$PH9D$0�  H�D$ H�D$0H��@  ����f�     H��@  J�<(����H�L$@H�qH;q�  H���O  H�D$ ��  �H�A�.���fD  H�D$ H��@  J�<(H������  H�D$ H��@  J�,(H�}X ��  H�M@H�U8H��H)�H��H��tR1��    H�<�L�$�    H��t%H����H�U8uJ�<"�" t
H��PXH�U8H�M@H��H��H)�H��H9�r�H���% �  H�EXH;R�% H�M`�{  H�UhH��$�   H�=��% ��H���% H������H�|$hH��H�������D$g A�   �D$, �$ �D$( �   �#����H�\$ H��@  J�<(H������  H��@  H�5�% J�(H9pX��  H�L$HH�qH;q�  H����  H�D$ ��$  �H�A������� ���D$, ��v=��  �b���E1��r����{x����  �    �k`�{a �$ �D$( �����H�H���PXH��$1�H�T$xH�t$XH���Ph�$�C8��$�   f.�$�   �P��D$(f.��$�������H�|$hH�5M� 袊�����  H�|$hH�5o� �   脑���C8���z���H�{H �D$, �����H���% H� H��h   �D$,�t���@ H����tH�D$ H��@  J�<(H�X �����H�T$pH�|$xH�5	� 讔��H�\$8H�{H;{��  H���  H�t$x�X���H�CH�t$8H��H�FH��$�   H�x�H;=T�% ��  H�D$@H�pH;p�k  H���  �   �H�@H�t$@H��H�FH�D$HǄ$�       H�pH;p�A  H����  �    H�@H�t$HH��H�FH�D$PH9D$0�����H��$�   H�x�H;=��% �n  H���   []A\A]A^A_�f�H�|$hH�5�� 1�������D$g E1��D$, �$ �D$( �   �?���D  ��6   t4H�@8�VUUU�t$`��  ɉ�����)ʍB%��  �@)Ɖt$(�q���H�D$ ��5   t2H�@8�VUUU�t$`��  ɉ�����)ʍD%�� )Ɖt$(�1�����8   u�D$`�D$(H�D$ ��9   ����H�@8�VUUU��  ɉ�����D$`)ʃ�����  )ЉD$(�����fD  1�����f�     1�����H�L$HH�qH;q��  H��t%H�D$ ��  �H�A�R���H�{H �D$,�����1��<���1������1������1�D  �1���H�|$hH�5D� �z����������H�|$hH�5I� �   �\�����������   ��� ����k`�$���H�ͧ H���я������H�T$xH��迏�������H��H�T$XH�|$8�X��������H��H�T$xH�|$8�A�������H�D$ H��H��  �x����0���H�D$HǄ$�      �H�pH;p��   H��� ����   �H�@�4���H��$�   H�=.�% H���v����~���H;q��  H��@ �����H�D$ ��  �H�A����H;q��  H�������H�D$ ��  �H�A�{���H;q��  H���s���H�D$ ��   �H�A�R���H�T$xH��蕎�������=��%  A�$   HǄ$�      �����H�=��% �e������n���H�=v�% H�K�%     H�H�%     H�E�%     �����H�=��% H�2�% H�5�% 膒���"���H���% H� H��p   �D$,�}���H�D$ H��H��$  ���������H��$�   H���̍��������@�    L�`�B�  �e���I)������H���% H)�I9�w}L��1�H��譈��L%��% ����H�D$ H��H��  �m����%���H�D$ H��H��  �T�������H�D$ H��H��  �;��������H�D$ H��H��   �"��������H��H��I9���   I9�H��I������IC�H���  H��$�   L��H�D$p�����I��I�| 1�L��H������H�5��% 1�H)��f  L�=��% L�1�I�I)��q  H�=��% L�H��t茁��L�-��% M�H���% L�-��% ����H��$�   H�=2� H�D$p�֒��H�=��%  H�WtQ����������s���H��$�   ������a���H�=��%  H�Wt-����������D���H�t$x�̏���5����P��J��H���뫋P��J��H�����H��H��$�   H��$�   H�x�豊��H��$�   H�t$pH�x�蛊��H��賒��H����H�=.�%  H�Wtw����������!���H�t$X�E�������H��H��$�   H��$�   H�x��D����H��L���7���H������H��u.E1�E1��S���L��H��L������L���y����P��J��H����I������@ AW�F�AVAUATI��UH��S��H��   ��H�T$8��  H�� Hc�H���H�D$8H��t	�8 �u  H�D�% H�0H���% H�8�ʍ��1�H�Ę   []A\A]A^A_�H�D$8H��t�8 t݁8��  t�H��P��uʋ}8����  �8  H�EHH����  H�uXH�|$8��H��% �   H�: t�L��D$��}���D$�H�T$8H��$�   H�|$p�D$p    Ǆ$�       ������1����K����D$p�Ed��$�   �Eh�   �0���H�T$8H��$�   H�|$p�D$p    Ǆ$�       �.�����1���������D$p�El��$�   �Ep�   �����H�|$8H��������? ��������D$p    Ǆ$�       A�   t
E1��A�ċE8������  H��� Hc�H���H�|$8H���X����? �O���H��$�   H�5� 1�Ǆ$�       �������  H���% � ����G8�����#���H�|$8H���������    tH�ExH��t
@8�u�H�T$(H�5'� L���Ɇ���D$`H�|$L����$�   �}��H�D$pH�x�H;=��% ��  HcT$P��~
� ���   H�ń   T��Z������H�|$8H��$�   H�5� 1��#������`���H�U �D$H����$�   �R`H���% �D$H�: �6���L���t���D$�%���D  H�}@H��tU��t�S�H��$�   Hc��gu��H�}@Hc��D� ������s�1�L��Hc��s������f.�� �D  �E �����H��$�   L��H�x��6��1�����H��H�D$pH�t$OH�x����H���1���H��H��$�   L��H�x���~��H������H�=��%  H�WtV����������H���H�t$O詃���9���H�=d�%  H�wt5��������������L��H�D$0�x���H�D$0�����P��J��H���릋q�D�F�D�A�����H��H��$�   L��H�x��[~��H���s���H�=�%  H�OtI��������������L���T$0�����T$0������P��J��H�������f.W� r�E �t����H��q��p����f.?� r	�E �T���f.4� r	�E �A���f.)� r	�E �.���f.� r	�E ����f.� r	�E ����f.� r	�E �����f.�� r	�E 	�����f.� r	�E 
�����f.� r	�E ����f.ܜ ҃��U �����@ f.�     AWAVI��AUATUSH��(H����  �y  I��H��D��E���   �A��D9���   ���I�L�������   A�~P tVI�F8I�V@H9�tIA��� H��H9�t7H��y" t�D��E��A��H��L��H�D$�5���H�D$I�V@H��H9�u� I��@  I��H  H)�H��H��t
A�@ ���  9�}���  @����  ��  �t�C(���  ��+�  �����  ��)����  D��  �ȋ{H)�A�   ��DǙ����DO���*   ��  ���  �SD�K AЍBA�A�P�A��D�A��DD�)�A)�A��D���؅��A�>  ���  �   �������M��CL9�O�������    ��Hy��Q�D���  L�%��% ���  9�Mʍ<0)�9�L�   9�O�E�D���M։��  Љ��  I�D$H�D$PH�CH�x� ��  L�t$PH�sL���6~����*   �i  H�s8�S$��  ��)�L���/�����  H��X  A�   H�E �D$    E1��D$    H�S81�H��H�$L���Ph��*   ��  E��t
H�E H���PH�-�% ƃ�  H�}H�u ��r���H*M�H*U�G� �H*EH�D$P�^�H�x�L9��^��X��H*M �X��\�f�fZ����  ��
  H���   []A\A]A^A_� H�S8�{D�K$D��  A�D���  +�  E�H)�����+C@���  ���  �N����C(ǃ�     �   �H������  )����  �t���D  H�s8��  ������fD  L�t$PH��L���|���]��� D�CLH��$�   H�Kx1�H��Ǆ$�       H��G� ���H��H  H��@  I��I)�I��E9�D����
  ��$�   E�����  ���  �L
  H�S8���  ���  D��  ������     H��$�   ���  L�K8L��$�   L��$�   L�D$@H��H�D$ ���  L��H��HǄ$�       HǄ$�       HǄ$�       )�HǄ$�       HǄ$�       �D$���  HǄ$�       HǄ$�       HǄ$�       HǄ$�       )�L���D$@ �$��k��H��$�   L��$�   H��$�   H��$�   I)�H)�I��H��I9���
  ���  H�s8�   +��  ��1���5  ��  �VUUU�< ������)��D%�� 9�}THc�   ��xIH��@  H��H  H)�H��9�}0H��H�<H��t#H������  ���  H�s81�+��  �f�1���8  ��  �VUUU�������)��   %�� 9�}(Hc�   ��xH��@  H��H  H)�H��9���  1���9  ��5  H��$�   L��$�   ��6  H��L)�H��H��t?I9�t2H�=��%  L���4  L�L$p H�H�z�L9��G
  H��H9�u�L��$�   H��$�   H��$�   H)�H��H��tH��$�   H��$�   H��$�   H)�H��H��tH��$�   D���  D+��  L��L�C8H�L$ L��H���v��L��$�   L��$�   H��$�   H��$�   M)�H)�I��H��I9���	  H��$�   H��$�   H)�H��I9���	  M���z  ���  +��  H�E H��h  �|$�{H�|$H�{8H�<$H���PhHc�   I�D$ǃx  ����H�D$`���  H��@  H��H  H)�H��9���   H��H�<H����   H�L�t$p�����   Hc�   H��H�@  H� �PdH�D$����   �pl���G  L�|$`H�5�� �   L�t$pL���o��H�D$�pd���1  �PhL��L�t$p�	h��H�D$�Hl��~1H�5�� �   L��L�t$p��j��H�D$L��L�t$p�Pp�pl��g����   ��x  �H�D$�xl����  fD  L�|$`H�s8�S$L��L�t$p��  �@)����H�E H��p  L�t$p�D$    �D$    A�   H�S8E1�1�H��H�$L���Ph��'   ��   ��(   ��   H�D$`H�x�L9��$	  H��$�   H��t�
e��H��$�   H��t��d��H��$�   L��$�   L9��]  H�=o�%  L�|$p��  @ I�H�x�L9���  I��L9�u�H��$�   H��������d�������@ L�|$OL�t$pH�5�� L��L���tq��H�s8�S$L����  �@)��z��H�E H���  A�   �D$    �D$    E1�H�S81�H��H�$L���PhH�D$pH�x�L9������H�=��%  H�W��  ������������L���r������ I�$H�M H��E1�A�   �P$�p �Q�S@��M���������C@    1�A�   ������1�������    ���  +��  E1�L�U H��h  E1�1�1�H��D$�CH�D$H�C8H�$A�Rh�v���D�C$E1�D9�}�{s t�K$A��A�   L�K8E��  A��E9�~
A����� E	�A��A��D9�v	����� D	�E��A��D9��8  ����  A�ۃ�A��E���h  ���}  Ic�   ��xI��@  I��H  H)�H��9��  A�V A�vA����_I�E�F(L���$    ��A��P8A�vA�V A����_I�A�N$L���$    A����P8A�vAv$A����_A�V I�L��E�F(�$    ��A��P8A�V AV(A����_A�vI�L��A�N$�$    A����P8A�^,A�V E1�AV(A�vL��I��D$    ���D$    ���ND�B�$�P@A�vA�V A��AV(A�N$L��I��D$    �D$    �$����D�B�P@A�V A�vA��AV(Av$L��I��D$    �D$    �$    �ND�B�P@A�vAv$E1�A�V I�L���D$    �\$�$    �ND�B�P@A�V A�vA��Av$A�~(I��D$    �\$�$    ׃��ND�G�L���P@L����X���T$XAVL��I�A�N A��  I��`  ��E1�E1��PpA�~\ �4  A�F �VUUUI��@  I��H  �D$ A���  D$ I�F8H)�H����  �������)�B�D$4�d$4��  ����  �\$41����D$@�����؉\$DH��$�   ��\$@�D$\�A�H�\$`L��H�D$(H���G@ H��@  N�, H�.�% I9EX��  H�C8��  CHD$ H;l$(��  H��@  H��I��A��I��J�< H����u�M��L�@  I�;�8��  ��  H��P��t�L�@  I�$H�pxH��t�H�H��t�H��H  H���  H�T$8��    H��H�@H��tH;H v�H�@H��u�H9T$8��  H;J ��  �D$XD�CA�D$D���  �L$ E��yD��  A�H�C8A���  �ȋJ(9NNN�J(�J,9NNND9J0DNJ0D9B4DMB49z8Nz89B<MB<�J,D�J0�B@    D�B4�BD    �z8�B<H�FH9BH�����H�
  �|$ I�A�L$�t$0D��H  E�F��WL�����PHH�C8��  CHD$ H;l$(����� I��I��P  H9\$8�  H�k H���c#  E1�L�l$8�%@ H���XP��I9�H����   H�k H���4#  H�}8 t�I�F8�u\�{(�S8�M`��  AFH)�A��AH�)���A)���AH���A�D
�A9�}��s0D�C<A�   �K4D�S@A)��CDM�L��A)�D�T$0�D$ )�A��D�$��E1�A�SxD�T$0�D$ M���{4+{0L�KHH�M0D)�H�U���U8I�L�����   H���O��I9�H���'���A��)   �5  A�~\ �*  A��5  ��u.A��7   u$A��8   uA��9   uA��6   ��  f�Ic�   ����  I��@  I��H  H)�H��9���  I�F8A�^H��  A�F A��  ���Ä���  A��8   �%  A��   E��L  D�D�ED�H  A�F�SA��  �l$4L��M��H�)�pA�RHI�F8��M���E��4  �݋�  A�FA��  �$   ��D�D;�����()����pL���T�A�R8I�F8M�E��4  ��  A�FA��  �$   ����׍H��T����t D�D;�L��A�R8A��6   �k  I�V8A��   E��L  A�FED�H  A��  D�l$4��  �SM��H��D)�pD�G�L��A�RHA�FA��  D����E��4  L��D)�4(I�F8�N��  �����I��$   ���T�A���P8I�V8A�FL��A��  M�E��4  ��  �$   D)������p�ʍH���A��A�R8I�V8A�FL��A��  M�E��4  ��  �$   D)������p�ʍH���TA��A�R8A�FA��  L��E��4  D)�t I�F8�N��  �����I��$   ���TA���P8A��&   A�������  A�~0 uE��4  E�F A�FL��A��  A���  M�D�E��  �H��p���A�RHA�F A��  L��A�NA��  E��@  �PI��$    �q�A���P8A�F A���  L��A�vA��  M�E��@  �A��  �$    D�A���PA�R8A��'   �  A��(   t_A�V A�vM�F8A���  A���  A��   E���   M��A�  �A��  D�G��D$�D$L��D�$����A�R@D  A��   ��x
H����������!�%����t��H��H�������  D�H�JHD� �H��H)���=��I�v8A�V$H�  �؍B�����H���% A�   E1�1�H��H� H�xH��   L��D$    �D$    H���   H�@8H�$A�RhH�H�% H� ƀ   H��$�   H�x�H;=;�% t)H�=�~%  H�W�Z  �������H�t$��F��f�A�F A��  L��A�vA�N$M�E��h  �PI�F8�  )�D��t0���A�RHA�F A��  E1�A�VM�L��E��l  �HI�F8�  H���% H� H��   A�Rp�����f.�     M�B�D�D��E��0  L��A�SHI�F8A��,  A��0  A���  E��0  ��  I��D���D�:L���PHI�F8E��,  ��  �����A�F��T$0E�l$D��L  A���ƋD$ D��A���t$h���ǍB�|$l��L��A��I�D��D�\$H�PHI��T$0A�L$E��A�   �L���$    �rD���P8I��L$ A�t$E��A�   �L���$    �Q���P8D�t$hD�d$lL��I��t$HD��<  �$    E��D����P8I�D��<  E��D��D��t$HL���$    �P8I�D��<  E��D��D��D��L���$    �P8I�D��<  E���$    �L$HD��D��L���P8H�C8��  ����M�F8��E��  A���H��   ��N�A�FX1���D����A�;E�E��,  A��  �A��0  A��  A��A�������I�V8A�~HIc�   A�^ A��  A�v��  �l$XE��   E���   M��9��H��I�@  �D�D�A���  �SF�D�L���t5�hD�\$D�\$D�$�A�R@H��}% A���   H� � �����I�V8���A�vA���  M�L��E��D  �  A^H��������T�Ic�   �\$XA�Ѝt3H��I�@  �X�$    �A�R8����A�~0 uE��4  A�F A�vL��A��  A��  M��N��P���D�@�A�RHA�F A��  L��A�NA��  E��@  �P�I��$    �q�A���P8A�F A�vL��A��  A��  M�E��@  �$    �P�D�@���A�R8����A�F A��  L��A�vA�N$M�E��h  �PI�F8�  )�D��t0���A�RHA�F A��  E1�M�A�VL��I���  E��d  �HI�F8�  A�Rp����H���   H�5�l 1��<������I�~8��A���  �D��  A���H��   ��N�A�FXD��ݙ����D��F�E��,  ��  B�A��0  D��  D��� ���A��0  ��M���E1�L���I��X  A�LB�T ���A�SpI�F8E��,  ��  �i����}8��  �Z  I�V8A�vA���  A�~HI�D��  �S�A؋\$XE�D8�L��D�T3Ic�   H��I�@  �^��D��PH�7���I�~8��D��  A���H��   ��N�A�FX1���A��F�
E��,  ��  B�A��0  D��  D������AN$D�D:�l$D�d$D�$L�����P@A�V D�\$0D�T$ �����A�V M�F8A���  A�vA���  A��   E���   M��A�  �A��  D�G����D$�D$D�$L����A�R@����A�V$A+��  E1�A�v I�L���N�����I��X  ��AV���PpD�\$ ������E��A���H�A�FX��ANșD�������D��A�;A�E��,  ��  �A��0  ��  A���  A��A���������A��9   �����A��   E��L  D�D�ED�H  A�F�SA��  D�d$4L��M�D��H�D)�A��pA����A�RHI�F8A�vL��E��4  ��  D�A��  ��������)�����D�I��$   D��)��P8I�F8A�vL��E��4  ��  D�A��  �����N�)�Ѓ����I��$   D�*�P8I�F8A�vL��E��4  ��  D�A��  �����L5�����D�I��$   D��)��P8I�F8M�L��E��4  ��  �����EfE��  ���$   �D�*A�L$B�t%A�R8�,���H��H�<H�����  1�E1�A��
9D$ }8A�E 9D$$|A�D9L$$��  D��  AE(��D)�9\$$|
9D$$��  E���f  ���    tyI�u8��  �9L$ �  A�}r tf��  A�M$����ލq9L$ |M�9L$ }EAE 9D$$};Aƅ$  H���  �$-��I�<$1��J���f�     ���)  A�}r ��  A�}T �����E���  A�E�H�D9L$ |NA�@9D$ DI�u8A�E ��  �9\$$~.A��  9D$$}!Aƅ&  H���  �,��I�<$1������f�A�}T uYA���  �9D$ |JD9L$ }CI�u8A�E ��  �9\$$~-A��  9D$$} Aƅ'  H���  �<,��I�<$1��b����A��D9D$ A����  E��  D9D$ �V���I�u8A�E ��  �9\$$�<���A��  9D$$�+���Aƅ(  H���  ��+��I�<$1������f��������I�B@H�������A�0A�W49������A�w@A�OD9�����E�G8E�O<E9��n���D9��e���D9��\���A�zX u-E1�9|$ �(���9T$ ����D9D$$����D9L$$�����\$$)�M�_HM�J0M�B)�L�T$(L�,$)�L�\$�ދ\$ )��߻   ��A��������L$I�<$E���   E�H���L$�<��������A�}\ � ���I�u8��  �4B9t$ �����A�u$A��A���B�4F9t$ �����AE 9D$$�����Aƅ   H���  �r*��I�<$1������    ���[���I�u8�����E��  ����E�M$F�
D9�}	��D)�A�U��yA�E    A�U(�49�}��)�A�E ���h���A�E     1��Y���A�E$Ѓ�9D$ ������y���9T$ �K���I�u8D��  B�
9D$ �2���A�E ����A���  ��~E�D$$A+��  A���  �����A��  ��  A���  �49���  )�A�MLL���C��I�<$H���  �S)��Aƅ)  I�<$1��D$�l���I�<$H���  �+)��1�����A�E$D��  �A��E)�D9L$ }r���    �����������     I�<$H���  ��(���   �����A���  �L$ L��)�AE@A���  ���A�}T I�<$tƇ�   H���  �@���9D$ }�A�E 9D$$|E� D9L$$��  AE(��D)�9\$$�\���9D$$�R���H���  �P(��I�<$1��v���D  A���   �"  �\$$�|$ A���  A�u(����A+��  )�AU$)�AE A�}s D�A���  I�<$E�E(A���  A�U$A�E �O D�O$�i  A�uD�VD9��q�A�uփ��    )�A�M�PA9�A�A�A�E D����    D)�A�E �A��  f.
Aƅ  1�A��9   �  I�<$�,��� A��6   thI�E8�VUUUA�uA���  ��  ��������)�������  )�9L$ }
  E1������D$?I��@  1�E1�D$@�    M��P  L9�u0�af.�     H����A���  ��uL�����H9�I��t3��I�G tӀxX u�I�<$�@X H���  ����L������H9�I��uͅ���  ��A�v\A�~q ���F  @����  �|$8��
  9D$0~`A�F$ȃ�9D$0}bA�F A���  �9T$4�v  �T$4A;��  �e  A�FL���������A�FLL��1�E1��E��I�$����A�v\��   ����A�F\�   �����AƆ�   AƆ�   �w���AƆ�   AƆ�   �b���AƆ�   AƆ�   �M���A��  ���D$,�a  AƆ�   I�$�   E1��
����}8�G����)  ����  �E  �}` �;  AƆ  AƆ�   1�AƆ�   I�$E1�������a   �   ����A���  �9D$0�����I�F8AN$��  )�9L$0�����A�F AF(��)�9L$4�����9D$4@������ �T$4A;��  �����A��  9D$4�����A���  A+��  A�VL9��D�����A�VL�f����     Ic�   �   H��I�@  H�8H��PpI�<$ �3�������Ic�   �����H��I�@  H�8H��PpI�<$ ����������{������  �}` �F  �}8��t����  t�� �/t
  H���  1����I�$�����H���  �   E1��l��I�$�D$>����AƆ�   AƆ�   ����������;L$0I�F8�j  ��  �499t$0�W  A�v 9t$4�I  �9t$4�=  �D$0AƆ�  AƆ�  fW�A���  �D$4A���  A�F$��~�*��A*F@�^�I�<$�A��  1�E1�H���  ���I�$������    �}` ����E������H�E �   H���PpAƆ�  AƆ�   AƆ�   �����f�     Ic�   L��E1�H��I�@  H� �pP���I�<$H���  ���I�$�M���H��L��������������}` ��  I�<$�   E1�H���  ����I�$����D  �}` �m���M�<$A��a   u�E��t�AƆ�   AƆ�   I��  H��t���M�<$IǇ      �E8H�=3/ D��   �Mc�J��    I��I)�M�P  ���I��  I�$H��  �B\H��  A�N,�J,A�N0H��  �J0A�VA��  H��  ���QI�v8A��   A�N A��  H��  ��  AvH����ʉW I�V8A�v$H��  ��  �ڍV�Q$H��  �JD�JHI�V8A�~DA�M0L��  ��  E�B ׉������p$���LF�L��A9�|��D)�A��A��A�A��D9�}A��A��D���9�}9��NэH��9�L�A�R(H��  H�j`H��  L�phH�E H�l$`�PX�,�I��  M�} H�L$HI��X  H�L$P�D$,I�EI9�H�D$@u!�   fD  L������H;D$@I����   A�W H�5�, H��1�����I�$I�O H�����E1�H��H��  ����I�$1�1�H��H��  �m��H��I��t�I�w(H��H�t$X��	��H�t$XI�}H������D$,A;G �e���H�D$HI�E(H�D$PI�E0�N���I�$1�ƀ�    �����}` �����I�<$1�E1�H���  ����I�$�����9t$0�����A�v 9t$4������9D$4��������E  �|$8H���  H���  H��1�E1����I�$����� L��AƆ   AƆ�   AƆ�   �H��Ic�   H��I�@  H�8H���������Ic�   H��I�@  H� �x` ������x8��  �����H�PxH�������H�xX��I�<$ �p����f����    I�F8��  �A9T$0|,A�V$A��A���B�B9T$0}A�V 9T$4|�9D$4�K���A���  ȍP�9T$0}/A���  A9��  �����A���  ȃ�����f.�     ��9D$0�I�F8A�V D��  A�D9\$4~�A��  9T$4}��D$0I�<$1�AƆ�  AƆ�  E1�A���  �D$4A���  H���  ���I�$�7���D  A��'   u]A��(   �����I�v8L�����I�N8��  �9�M�A���  A+��  A�V@9�}1�+�  I�A�V@L���v�������I�v8L������I�N8A���  A+��  ��  9�LƉ�)Ɖ�A�v@��  9�}A�v@��9�}�1�+�  I�A�F@뛋D$0AƆ�  AƆ�  A���  A���  A�FLA���  �W����T$4�t$01�L���U��AƆ�  AƆ�   �2���I�F8�VUUUA��  L����  ɉ�����)ʍB�T$4%��  ��)�����_���H��M% H�
I9�tsH�TJ% H�2�   I9�tN���I�$�����H��M% H�0I9�t|H�#J% H� I9�tPH�\I% L;�   t�H���Z��I�$����H���I��I�$�x���H�
��I�$�)���H�
��I�$������T$4�t$01�L����������f�SH��H��H% H� H�x �  D�H$E����   �@ ����   ���   H��tr1���*   u_�{9�X{$9�}Q�K D9�IK(A9�}A9�~u�CL��~��H�߉CL� ������   �   t�   H�߉$�����$�    H��[�f�D�D$�L$�T$�4$�; ��D�D$�L$�T$�4$�d���fD  }.���  +��  �SL9�}���H�߉SL�}����x����     t��i���f�     H�
����  H�������   ������9��/����P����  +��  �KL��   9�������H�߉KL�������   �����H����Hc�   �M  H��H�@  H�(�}a ��  �}8����  ��  �G�����  �������  H�E H���P8����H����Hc�   �  H��H�@  H�(H�E H���P8��������E8�P����x  =��  �m  H�C8�SH�   �K ��  H�ߋ��  ��  ���   љ��D�$�C���  ���P���9�D��M���)���E����   �   H��������   � ����     H��H�@  H� �xP ������@PH���l����   �����H��H�@  H� �xP ������@P H���>����   ����H��H�@  H��H� �pP�����   �����CL1�ǃ       ���2�����H�߉CL�������   ����H�E H���P8���&����{\ ����H�E ����x�������   �   �-���H�E D�k\�   H���PpI�$H�������E��������I���H�
�%D  M��I�uH������M�uL������M��u�L�mH������M��tL���I�l$L���m���H��tI���I�oL���W���H��tI���s���H�D$ H�hH���9���H��t
H�l$(�m���H��8[]A\A]A^A_�f.�     D  AWAVAUATUH��SH��H��H�OH;OtrH��teH�y�H�q�H�yH�1H�}H�GH�EL�2D�j�jH�W�H)�H��H��tH��H��H)�����L�3D�k@�kH��[]A\A]A^A_�fD  1��@ H�H)�H��H����   H�<	H9���   I��I������I)�I��L��H�T$�����H�T$I��I��M�tL�
L�RM�M�WH�u H��E1�H)�H��H��tI��L��I��L���[���H�MO�D&E1�H)�H��H��tI��L��H��I��L���.���I��H�} M�H��t�*���M�L�u L�eL�m�	���fD  I��A�   I)�I���<���H��������H9�����I��I��E1�I)�I��I��H���!�������fD  AWAVAUI��ATUH��SH��H��HH�H;{��   1�H��t
H�|$��������L�d$?H�������H9�tIH�L��H��H�x��������H�<�    H�D$����H���n�������H���a���H��������������H���G�������H���:�������D  AWAVAUATUH��SH��H���L  L�gH�GI��H��L)�H��H9��@  L��D�)H)�H��H9��Z  H)�L���   ��H��H��H�ڃ�H9�HG�H����  H��E�,$H�u�I�T$v^H��E�l$H�u�I�T$vJH��E�l$H�u�I�T$v6H��E�l$H�u�I�T$v"H��E�l$H�u�I�T$vH�u�I�T$E�l$H9�teI��I)�M��I��N��    M��t4D�l$I�<�1�fnL$fp� H��H��fG�L9�r�L)�M9�J��tH��D�*tH��D�jtD�jI�VH�<�1�H��I�~��  H�I9�I�n��   H�CH��L���H)�H��H��H��H����H9�H��HG�H����  H��D�+v@H��D�kH�Cv2H��D�kH�Cv$H��D�kH�CvH��D�kH�CvH�CD�kH9�teH)�H��H��L��    M��t1D�l$H��1�fnT$fp� H��H��fA�H9�r�L9�J��tH�PD�(I9�tH�PD�hI9�tD�hH��[]A\A]A^A_� H�H��������?H��I)�I��L)�H9���  I9�H��I��IC�I)�I��I��p  I������L��H�L$�����H�L$I��K�T� �1I��H�у�H��H�ك�H9�HG�H���  I���2H�E�H�JvOI���rH�E�H�Jv>I���rH�E�H�Jv-I���rH�E�H�JvI���rH�E�H�JvH�E�H�J�rL9�taI��M)�M��I��N��    M��t3�t$J�<�1�fn\$fp� H��H��fG�L9�r�L)�M9�J��tH���1tH���qt�qI�6H��E1�H)�H��H��tL�<�    L��L������I�FM�I��1�H)�H��H��tH�,�    H��H��H���U���H��I�>H�H��t�R���M�M�.I�nM�fH��[]A\A]A^A_�D  H�,�    H��H���
  I�T$8I�D$@H)�H��H9���   H�<�L�4�    H����   H����I�U8u.I�M@f�H��H��H)�H��H9�r�H��[]A\A]A^A_�fD  I�D$8N�<:H�T$J�0I�H�qH�$������H�$H�T$u��APH��L��H��A�GP����I�L$@I�T$8H��H)�H��H9�s@I��@ J�<2H��tH����u&I�T$8I�L$@H��H��I��H)�H��H9�r�D  I�U8I�M@�+��� A�D$PA�EP�D���f�     H��H��% �N!�V H�5�� H�81��<���1�H���D  USH��H�s% H�H��tH��p  �O���H9�tH�=�����n���H�w;% H��[]�H�H��p  ����H�1�H��p  �`�����@ f.�     USH��H�% H�H��tH��p  �����H9�tH�=;% H��[]������     H�H��p  �1���H�1�H��p  �������@ f.�     H����   �zX uQ�z[ t#�B@�   �RP�JHH��� �k��� �Z�   �R�J�H��� �D���@ �B ��   �Z8�   �R0�J(H��� ����fD  � �G ��     AWAVAULc�ATI��USH��H  �d���I��$p  L�|$0H�\$H�c� M��H��Hc��   H��H��   H�hPH������I��$p  A��H�D$ H�.� M��H��H��H�D$�}�����u�D   H��H  []A\A]A^A_�E��t�L��H�# L�J H��$�   L�t$@E1�H��
�     F��    E1�E1�1�A�4Hc�H��2��A	�A�0����A	� u�F�T= G�>I��I���   u�I��$x  I��$p  L��A�    ����I��$x  I��$p  H��A�    �    I���a���H��H�g� H�L$I��$p  I��L��F��H�M� H��B��$�z���I��$p  L��H������I��$p  H�������2����D   H��HE������L�T$M���  ���   t��fD  @����   �AD��E���A*����A��   ��A���ABD1�)�D��AƂ�   �A��   D1��ABD)�9��A*��A��   N��AB�*��A��   �*��8* �\D* �X��\��*��\��^��Y
�SHD���H�u[]L��IvI�V(A\A]A^�?����    []A\A]A^��    H�u��1���@ f.�     USH��H��8@��tBH�l$��H�� �   1�H���U���H�T$H��H���D$/ �`���H��8H��[]�fD  H�T$H�5�� �?���H��8H��[]Ð@ UH���x   SH��H���:���H���tgH�H�PH�SH�PH�SH�P H�SH�P(H�S H�P0H�S(H�P8H�S0H�P@H�S8H�PHH�S@H�PPH�SHH�PXH�SPH�P`H�SXH�PhH�S`H�PpH��H��H��[]����f�f.�     H��tcH�d% �    �G    �G  �G    �G    H� �G    �G    �G�   �G �G H���Gt�8���G�G$   �H�w(��fD  �   ���     SH�������H��t�C �C �C [�@ SH������H��t�C �C�C[�@ AWAVAUATUH��SH��L�gH�L��H)�H��H9�rvH��H�GH��[]A\A]A^A_�H)�I��t�H�GL)�H��H9�w H��    L��1�H������H]�D  H��������H��H)�H9���   H9�H��I������HC�H���   L���=���I��I��I�|� 1�L��1��%���H�u L��H)�H��H��tH��    L��H�������H�EL�I�1�L)�H��H��tH��    L��L��H������H�} I�H��t����L�m M�L�}L�m�����@ H9��Z���H��uE1�E1��U���H�=:� �����L�4�    �1���fD  AWI��AVAUI��ATUSH��xH��H�t$(��t	H���F  H��% H�; �"  ���  H�|$(�  M��H�$�  I�E L�����H�$��  H�H��m۶m۶mH��  H��   H)�H��H��H9���  H��H��A�{H��A�G H)�H�H�XH�D$H�@H)�H��H��H���\  I�E1��$ L�d$`H�T$ H�D$H�D$PH�D$H�D$OH�D$8H�D$(H��H�D$0�   @ L�sXM��tOL�������<$ M��tH�PA�,H��A� L�CXL;�% H�K`�O  H�t$(H�ShI�<H)�A���$L���P���H�\$0H9��B  H�D$PH�x�H;=�
% �r  H�D$H��H�XH�@H)�H��H�D$ H9��m  H��    H�t$H��H��L��H)�H��3����.   �   L������L�t$L��L������H��L���f���H�D$`H�x�H;=
% �  I�E 1�1�H�t$PL���PH��H���?���H� H����������L���i����<$ I��tH�@C�7,I��A� H��	% L��H��H��H�D$`H��PP�C8����w+H�5�� L�����������   H�5�� �   L�������H�\$`H�T$(L��L)�H�������H�{�H;=I	% �w���H�=�%  H�G��  ��������Ѕ��R���H�t$8������C����    H�D$(L��H�X�����H9���   A�}A�D H��x[]A\A]A^A_�@ � �����     H�5�� L���!������=���H�5�� �   L�������$���H�t$(I�<H��H)��ܾ�������    H�D$PL��H�x��_���L������H9��`���H�D$(A�.A�D�.A� H��x[]A\A]A^A_�H�=�%  H�W��   ����������k���L��������^���H�=�%  H�WtO��������������H�t$8��������H��H�D$`H�t$OH�x�����H�D$PL��H�x�����H�������P��J��H����H���ՋC��P��S��W���H��H�D$`H�t$NH�x��g���H������H��H�D$`H�t$H�x��I���H���a���H��H�D$PH�t$OH�x��+���믋P��J��H����
�����     SH��H��@�� �*S�*K�*�^�H�T$0H�t$ H�|$�\$�D$    �D$     �D$0    �^��^�������D$�,��\$=g  ~e-h  �C�D$ �Y��
  D�CH�>D��8W"��  �W"H�AH�L$H�@8H�P�C���B"H�AH�@8H�P�C���B"H�A�SH�@8H�@�P"H�A�SH�@8H�@ �P"H�A�SH�@8H�@(�P"H�y ����H�L$H�AH�@8�sH�P0@8r"t@�r"H�y H�L$轲��H�L$H�AH�@8H�@8�x` ��  �sfD  @���{�KD��   �t
H�G8    ��`� f.��  ��w�Y��Y��o�Y��X��X�f(��Y��X�f.�w�f)L$ H�|$�\$�d$�N���H�|$f(�fW��d$�YG8�\$f(L$ f.���   f(��\$ H�|$f)L$0�d$�T$�j����d$H�|$�T$�^�f(L$0�X��Ykk �\$ f(��Y�W8fT��G �G�Y��Yg�G(�<k f.��g0������{����     �5�� �w8������� fW��=����f�f.�     H��SH���F  �� H�G    H�G    H�    �G�C H�G0    H�G(    H�G8    H�GP    H�GH    H�G@    �GX �GY �GZ�G[ H�Gp    H�Gh    H�G`    �Gx ����G|  �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?H���   蘾��H���   ƃ�    ƃ�    H��tAH���$ H�P8H���$ H���   H�PHH���$ H���   H�P@H���$ H���   H�PP[Ðf.�     H��SH���F  ��� H�G    H�G    H�    �G�C H�G0    H�G(    H�G8    H�GP    H�GH    H�G@    �GX �GY �GZ �G[ H�Gp    H�Gh    H�G`    �Gx ����G|  �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?H���   �8���H���   ƃ�    ƃ�    H��tAH�o�$ H�P8H�D�$ H���   H�PHH���$ H���   H�P@H�H�$ H���   H�PP[Ðf.�     H��SH���C  �+� H�G    H�G    H�    �G�C H�G0    H�G(    H�G8    �C@H�GP    H�GH    �GX �GY�GZ�G[H�Gp    H�Gh    H�G`    �Gx ����G|  �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?H���   �ۻ��H���   ƃ�    ƃ�    H��tAH��$ H�P8H���$ H���   H�PHH�=�$ H���   H�P@H���$ H���   H�PP[�@ f.�     H��SH���C  �˻ H�G    H�G    H�    �G�C H�G0    H�G(    H�G8    �C@H�GP    H�GH    �GX �GY�GZ �G[H�Gp    H�Gh    H�G`    �Gx ����G|  �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?Ǉ�       Ǉ�       Ǉ�       Ǉ�     �?H���   �{���H���   ƃ�    ƃ�    H��tAH���$ H�P8H���$ H���   H�PHH���$ H���   H�P@H���$ H���   H�PP[�@ f.�     SH��H��0�_ �W(f(��O0f(��Y��Y��X�f(��Y��X��%�� fT�f.e w3�-+� H�G    H�G    H�    �oH��0[�f�     ��d H�|$(H�t$ �L$�YC8�T$�\$�Yg� �ک���D$(�\$�T$�L$�Y��Y��Y��d$ ��c�S�KH��0[ÐfD  USH��H��H����   H��H��t~�B����*  �B�����  H����  ���|  H�AH���o  �~[ tI�{Z ��  �    �K@fZ��M �SHfZ��U�[PfZ��]fD  H��[]ÐH�x8H�P@H)�H��H���  L�G �VXE�H"�փ�A8�uPL�W(E:J"uFL�W0E:J"u<L�W8E:J"u2L�O@A:Q"u(L�OHA:Q"uL�OPA:Q"uH�X:W"��   �    A�p"H�AH�$H�@8H�@(@�p"H�AH�@8H�@0@�p"H�AH�@8H�@8@�p"H�AH�@8H�@@�P"H�AH�@8H�@H�P"H�AH�@8H�@P�P"H�AH�@8H�@X�P"H�y �3���H�$H�AH�@8H�@`�x` t
���H�x8H�P@H)�H��H����  L�G �SXE�H"�փ�A8���  A�p"H�AH�L$H�@8H�@(@�p"H�AH�@8H�@0@�p"H�AH�@8H�@8@�p"H�AH�@8H�@@�P"H�AH�@8H�@H�P"H�AH�@8H�@P�P"H�AH�@8H�@X�P"H�y 臎��H�L$H�AH�@8H�@`�x` ������@` H�y �_����C[�����fD  H�r :F"��  �F"H�QH�R8H�R(�B"H�QH�R8H�R0�B"H�QH�R8H�R8�B"H�y �
����|���D  H�zp:G"����H�Rx:B"������{Z t`�} H�KH�SH�sH��Z��{@�MZ��KH�UZ��SP�E �U�MZ�Z�Z�荜���<����     �E H�KH�SH�sH���C@�E�CH�E�CP�U�M�E �?��������f.�     L�W(E:J"����L�W0E:J"����L�W8E:J"����L�O@A:Q"�����L�OHA:Q"�����L�OPA:Q"�����H�X:W"������L���f.�     H�z(:G"�p���H�z0:G"�c���H�R8:B"�V����{Z ���������H�
  �A6�AnfZ�fZ��^��^��t$�AvfZ��l$W��l$�^��t$�AvfZ��^��t$�5~� �t$�AF`�AVh�Y��Y��ANp�Y��X��X��Q�f.���  A�~[ �   uf.-(F ���l$`A���   H��$p  �l$L��(�H���E��   H��$�   �AY��A^|�E��   ��DY��E��   �A��   ��$�   �X��E��   �DY��E��   �A��   �Y��A��   ��$�   �AX��Y�H��$�  H��$`  ��$�   ���$�      ��AX�H��$�   ��$�   �   ��$�   H����AX�DZ�(��AY���$`  ��DD$H�X��Y�Z��AX��\$0�AX���AX�Z�(���$p  ��AY��|$@�X��Y��AX�DZ��X��DL$(�X�(����$�  �Z�DZ�(��t$8�DT$ H��$�   H���y� �T����|$@��$`  �t$8�DT$ f(�f(��DL$(�AY�Z��AY��\$0�DD$H�Y��\��EY�fA(��Y��l$`�AY��Y���$p  �A\�Z��\��Y�fW��X���$�  Z��Y��X�f.����$�   ��$�   ��$�   ���f  HǄ$�       M��E1�M�$�   E��D$0A��Ǆ$`    �?Ǆ$p      Ǆ$�      ��  A����  �T$(��D$H��$�   H��$�   M��H��$�   �t$�l$�d$�\$�Ҏ��Ǆ$�       �=� ��$p  ��$`  �AL$|�A�$�   �Y��Y���$�  �X��A�$�   �Y��X��A�$�   �Y��AY�$�   ��$   �A�$�   �Y��AY�$�   �X��A�$�   �Y��AY�$�   �X��X��X���$  .D$��$   �/   �t$0�   +�$�   ��t1�t$.�s&Ƅ$�   �-fD  �L$0��$�   ��u�.D$vڃ�Ƅ$�    ��  H�H���������L�@I��L��Hk�$ M��L��$ H�H�pH��$�   I�M�[H)�H��H��I)�I��H��I��H��H��H�L9���  L��H�$ L�H�@L)�L�L$8H��H9���  L��Hi�$ L�H�@L)�H��H9���  ��VUUU����)����  �B�E1��*�$�   ��$�   �*�$�   M��H�@�-<� �t$x�*�$�   H�D��l$t�t$HH�D$@H��$   H��$�   H��$  H��$�   H��$   H��$�   H��$P  H�D$hH��$@  H�D$PH��$0  H�D$`L��M��H��I���~  �    .D$vǄ$     @���? �AA����$0  �Ao��$@  �Ag��$P  �H  A����  H��$�   H��$�   �|$ H��$�   �t$�l$�d$�\$胋����$  H�T$h��$   H�t$P�M|H�|$`���   �Y��Y���$   �l$�d$�t$�X����   �Y��X����   �Y��Y��   ��$   ���   �Y��Y��   �X����   �Y��Y��   �X��\$�X��X���$P  ��$  ��$@  ��$   ��$0  葊����$@  �T$0��$0  �M|���   �Yȅ��Y���$P  �|$ �l$p�X����   �Y��X����   �Y��Y��   ��$0  ���   �Y��Y��   �X����   �Y��Y��   �X��X��X���$@  ��$�   �Y���$P  ��$   �Y��Y��X��L$x�Y��X��,�B�c��$  W<� �Y��Y��X��\��,�B�Dc�X  �L$��� ���� �YD$H�t$�Y��Y�� .�s.�� �����s�H,�������� E����  ��  �A��� � �D���$P  �l$t.��t$(�s	.���  �   ��|$(�T$ ������T$ I��I��H�L$8�|$(!�B�!I��L;l$@�a  �AE ��$�    ��$   �AM��$  �AU��$   �N����t$.��Y���Ǆ$     @?��� �A���W��5t� �t$�t$�l$�l$�l$�l$������%;< �AVh�ANp�A^`f.�fZ�fZ���  f�fZ��Z��^��-u� ��$�  ��$`  ��$p  )�$�   W��fW��Z�Z�f(�f(��Y��Y��X��X��-�; f.���	  �������� fW�f(��Y� H��$�   H��$�   �T$8�\$0�L$(�a����L$(��$�   �Q�f.��t$ ��$�   �\$0�T$8��	  �^��l$ fW�fZ��Y��Y��Y�f�f�f��l$HfZ�fZ��t$`fZ��l$P(��D$�t$h�t$HH��$�   L��$�   H��$�   �L$Ǆ$`    �?(�H��Ǆ$p      �l$`L���\$hH��Ǆ$�      �0���H���t$�l$L���d$H���\$L����$�  ��$p  ��$`  ����Ǆ$�       ��$p  ��$�   �   ��$`  ���M|H������������   �Y��Y���$�  �l$)��X����   �Y��X����   �Y��Y��   ��$   ���   �Y��Y��   �X����   �Y��Y��   �X��X��X���$  .���$   F�H�L�@I��L��H��$ M��L��$ H�H�pH��$�   I�M�[H)�H��H��I)�I��H��I��H��H��H�L9��W  L��H��$ L�H�@L)�L�L$xH��H9��1  L��H��$ L�H�@L)�H��H9��  ��VUUU����)���N  �B�E1��*�$�   ��$�   �*�$�   I��H�@��$�   L��$   �-�� H�D��=K� ��$�   H��$�   H��$   H�D$@H��$  H�D$8H��$P  H�D$0H��$@  H�D$(H��$0  H�D$ H��L��I��M���  @ �Yx7 �\�� �Y
� ��$0  ��$   (�����H�
(�T�� B�t5 �   ��|$hI���"}��#�$�   �|$hC�4I��L;|$8�����H�3�$ L�XL+I��H�I�$ �9��8D��$�   L��H� H�xD��A����L���H����D)�A�RP�����O* Ǆ$�    �?��$`  W��T$�O����%* Ǆ$p    �?��$`  W��L$�%����j~��f(�����f(��X~���`���H�
H��u'H�GH���H�
H��u'H�GH���H�
   ����H�5�� H���d�����   �����E  0��������     H��H� ��   H����   �OxL�W`H�WpH�G8H+G0L)�H�ыOhH��H)�H9���   ��*   ur�~ tL���  H��txI���t]1ɿ   � �    H��I��H��I��M��I�L��t?H��H9�uމNX���FX����Ɔ*  H��H���b��fD  H���M��A�   1�L	�I���1��H�
  ��h��H�
  ��h��fD  USH��H��H� ��   H��H����   H�w`H�Gp�WxH)�H�GhH)�H�G8H+G0H��H9���   ��*   t^�} t<���  1҃�Iщ��  �MX��x!9�}HcѸ   H��H��H��H!��    ƅ*   H���ya���}T tƃ�   H��[]�H�
  ��g��H�
  ��g���fD  UH��SH��H� tFH��H��t>�~ t��*   u�F �{T uƅ�   H��[]Ð�+l���C H��H���|f����H�
  �Kg���f.�     UH��SH��H� tVH��H��tN�~ u"��*   u)�FH���m`���{T uƅ�   H��[]�f�     �k���CH��H����e����H�
  �f���f.�     AUATUH��SH��H� �j  H��I���}  H�u(H�w8A��H�W01�H��H)�H������   f�     H��H��    H����   H�P8E��D��  L�h8H�E0H�<��   �q t?D��A+�  �@�������GH�E0H�D��A+�  �������B H�E0H�<�p tN�G$A��  �A���G$H�E0H�4�F(A��  �A���F(H�E0H�4�F@A��  �A���F@H�E0H�<��^��H�U0H�u8H���KH��H)�H��9�����H�ը$ H�H���   H��t�1a��H�ƀ  ƀ�   ƅ�   H��[]A\A]�H�
Z�����n  H�5�� H����Y������  H�5�� H����Y������  H�5А H����Y�����l  H�5�� H���Y�����U  H�5�} H���Y����t=H�5� H���Y����t*H�5�� H���qY����tH�5�� H���^Y�����&  ǅ�      �   ����f�H��������; �|���H�5L� H���Y�����G  H�5:� H���Y�����0  H�5,� H����X����tH�5� H����X������  ǅ�      �   �$���f�H�������; �����H�L$H�5�} 1�H��H���Ed�����\  �$���Q  �T$���E  ���   ���   �   ������    H����   �; �|   H�5�| H���7X�����w  H�54d H��� X�����`  H�5Ɛ H���	X����tH�5�~ H����W�����N  ƅ�    H��(�   []� H��t	�; �j  H��$ H�0H�)�$ H�8�qf��H��(1�[]��     H��tр; t�H�5%| H���W������  H�5�c H���pW�����p  H�5� H���YW����tH�5E~ H���FW������  ƅ�    H���$ H�0H�F0H�N8H9��5���@ H�H��t���   �JsH�N8H��H9�u��
H��H;h�t6��H9�I��u�H��$ H��H�0��^��1�H��[]A\A]A^A_�1�fD  H��  H��tH9�t�z^��I�M Hǁ      H��H���pZ��M�M 1��   M�A`A�AhE�yxM�Qp� H����H��I�tC��?�Pt3����M9�u�A9�u�H�
��4
H��H�U H9�u�L�ZHA�H�} H9�  tLH���   H�~� t>�Y����x5H�U H�J0H�R8H)�H��9�}H�H�<�H9�tH��t�IK��f�     []�   A\�H�Ǒ$ H�0��V��1�����H���$ H�8��U��1�����H�
A����  E����  H�Ǎ$ D;(��  H�y8H�Q0H��  I��I)�H�D$I��D��D����  H�qHA��H�I��I��H�4�1� LcJ��H��t�{ t
O��1�H��� AVAUATI��USH��0H���$ H�;H����  M����  H�W��H��tmH�H���P ��t]L�t$ I��H��I��L����F��H�H�xH��R ����   H�H�xH��R ��t H�ى$ H�;H�0�nO��H�;�f�     H�;���  ~2����   ����   L���(K��H��0�   []A\A]A^�fD  ����   L���N���   H��0[]A\A]A^�D  L��L���
H��H;x�t&H9�u�H�چ$ H��H�0�M��1�H����     H�H���H�Y�$ H��H�0�vM��1���H��$ H�8�cL��1���D  f.�     H���$ H�H��tH�B8H+B0H���@ H��H�͈$ H�8�L��1�H���fD  H��H���$ H�H��tA��xH�J0H�B8H)�H��9�|H���$ H��H�0��L��1�H���D  Hc�H��H���H�]�$ H�8�K��1���f�     SH�H�$ H��H�;H��t*��N��Hc�1���xH�H�r0H�R8H)�H��9�}H��[�H��$ H�8�TK��1�[�USH��H�-�$ H�M H����   H��tiH�A0H�Q8H9�t#H;8t?H���f�     H��H;x�t&H9�u�H�*�$ H��H�0��K��1�H��[]�fD  �{<��H���   []��    H�Y0H�A8H9�t� H�;H��t
�    H����t
1�H��f�G���t� ���    � H���g@��t��1�H����f�O�@��t�D  �    ��H��됐�     SH��H�H��t�{ u]H�{H��t�/G����H�ߺh   ub@��ul@��uF��1������H�t
�    H����t
1�H��f�G���t� [�@ ��F���f�     �    ��H��뫐H��� �g@��t��1�H����f�O�념ATI��UH��SH�1�H�FH)�H��H��t7�H��H��H��H�H�:H�rI��$x  �>E��H�U H�EH)�H��H9�r�[]A\Ð@ H�    H�G    H�G    H�G    H�G     ��     H�    H�G    H�G    H�G    H�G     ��     ATUSH��H�� L�%�$ I�<$H����  H����   H��H����   �> ��   H��  H��tH9�t�
��  :T$��  :T$��  ��'t��"t��`A����   D  �E A��A�ԉD$A��D$�U  �    I��A���
t��	t.��
t[A�1�����H��}$ 1��A����D  ��
��   ��	��   H��}$ �AD��+D$H��([]A\A]A^A_�@ �E �   A�   �;��� E��E1���m�������D  �D$�E �D$A��D$H��([]A\A]A^D)�A_�@��u�E A�   �}������u�����
�:���@ �~��� ��
�%���I��A���
t��	t��
  �{  A��  �^  A��  �A  A��
H��x   u�D   �fD  �[N���f.�     ATUS���   H��uH��p   t
H��x   u[]A\��    L�%_$ I�$H��tH��p  ����H9���  H�=DL��1��
�$ ���H��p  �Z   H���  ����H��p  �F   H���  ����H��p  ��   H���  ���H��p  ��   H���  ���H��p  ��   H���  ���H��p  �   H���  �u��H��p  �   H���  �]��H��p  �\   H���  �E��H��p  �:   H���  �-��H��p  1�H���  ���H��p  �   H���  � ��H��p  �D   H���  ����H��p  H���  ��   ����H���  f�     �uH���#��H����  H��H��u�1�H����"���   H��X  H����"��H��`  I�$ƃ�  H��tH��p  �"��H9�t7[]A\H�=R�$ �=��I�$H��p  �}"��I�$1�H��p  �; �������I�$H��p  �V"��I�$1�H��p  � ���f�AUATUH��SH��L�-�\$ I�E H��tH��p  ���H9���  H�=�I��L�e`H�����H���  H��p  H���$ �"��H���  H��p  �~"��H���  H��p  �k"��H���  H��p  �X"��H���  H��p  �E"��H���  H��p  �2"��H���  H��p  �"��H���  H��p  �"��H���  H��p  ��!��H���  H��p  ��!��H���  H��p  ��!��H���  H��p  ��!��H���  H��p  �!��D  H���  H��p  H���!��L9�u�H��X  H��p  �y!��H��`  H��p  �f!��I�E ƅ�   H��tH��p  ���H9�t=H�=N�$ H��[]A\A]�/��I�E H��p  �o ��I�E 1�H��p  �-���J���I�E H��p  �H ��I�E 1�H��p  ����@ ATUSH��H�����   tH��p   t
H��x   uH��[]A\��    H�t$�n��H�-wZ$ H�U H;�p  u��5��I��H�E H�t$L;�x  t#H������H�E H��L��x  H��[]A\�M��H�t$�G��H�t$H��x  H��p  H��H������H��[]A\��G���     H��W$ H�8H��Y$ H� H��tH��h  ��@ f.�     H�qX$ H�8H��Y$ H� H��tH��p  ��@ f.�     AWW�AVE1�AUATM��USH��x  L�-G]$ L�=�X$ (5�� �9� I�E )�$�  )�$P  )�$�  (5v� I�EI�)�$�  (5p� I�GH��$P  )�$�  (5e� (
H�JI�EH�6H�D$H)t$H�|$H�|$ ��   H�D$    H�D$    H�D$     �B��H�|$�  H�U H�EL�t$A�    H)�H��H��t=M��1�I�� L��Iu H��H��H�L����
��I�$H�Q0H�y8�����f.�     �B��������z���z����|$9:������|$9z�����;D$������D$9B������]���D  H��E$ H�8�!	��H��x1�[]A\A]A^A_�H�iG$ H��H�0��	��H��x1�[]A\A]A^A_����H��p  I�$����f.������^���H�
���   �_���f�H�5U. �
   H����
���   �?���f�1��)���f�     H��H���������H�5�. H���A����   �����    H�5� H���!����   ������    H�5�- H�������   ������    �
; H�5�+ H�=�, �   ����H�
�!  L���f.�     ��
�	  H���
��u��D$(    �D$/ H�=x:$ 1���D$@   �D$P   L�t$@HǄ$�       HǄ$�       H�WH�|$PHǄ$�       �D$`����H�|$H�|$pH�T$pH��$�   H�|$H��$�   H�|$ ��   ���    �=   E�H�L$A��H�|$�D$    E��$A��A��L��L���H����� �7  L�D$pI�x� ��  Hc�1�E�<E�����Hc�L�E����  ���!
  H��$�    �1
  H�|$ H�5�& 1�Ƅ$�    ����H�T$pH��$�   H��$�   H��$�   �Y�����A����  ��$�    t|L�l$pH�5# L��������tH�5S L���m�������  A��	t
A�� �u  ���	�q  H��fD  H�����	t�� t��=�F  Ƅ$�    fD  H��5$ H�8�	�����$�    A�    �L$`LE�$�   H��$�   E��H��$�   H��$�   I����������  H��2$ �T$P�0fD  ��������	tC< t?<
�T$P��  ��I������H��4$ �   L��$p  H� ƀ�   �  D  H���<	t<
��������H�T$=H��$�   H�5" �����M��t7L��f��+����=   L��H�������H��H��tI9�tH�E�H=�  �K  H�O2$ 1�H�8���������� ����������|$/ H�\$p�L$@��  L��$p  H�� �   1�L������H�T$>H��$   L��Ƅ$�   ����I��H�1$ H��$   H�R �   1�H�������H��$   H�x�L�����������H��0$ H�8�+���H��tH��0$ H�8�����8 �<  �T$@�t$(H��$0  H�\$p��#��I��H��0$ H��$0  H�� �   1�L��$p  H���4���H��$0  L��H�x��0����H����T$@�t$(H��$   H�\$p�#��I��H�/0$ H��$   H�  �   1�L��$p  H�������H��$   �	������H�\$p�  �����  �T$@�t$(H��$�   �#��I��H��/$ H��$�   H�W �   1�L��$p  H���h���H��$�   L��H�x��d����|���H�T$?H��$   H�5� �V���L��$p  �>���H�J/$ 1�H�0�h�������H��H������H��I������H�5� H��H��L��1�L���"������  M��tL���|���H��tH���o���H��.$ H�8 ������K���f�     �T$@�t$(H��$�   �"��I��H��.$ H��$�   H�z �   1�L��$p  H���[���H��$�   L��H�x��W����o���H�;.$ H�8�����I9�������T$@�t$(H��$@  H�\$p�!��H�	.$ H��$@  H�8����I��H�'.$ H�� H��I��   1�L��$p  H�������H��$@  ����H�=� �   L���������=   H������H��t�  �
 L�����������k�����$p  f.
  �|$,��
  �D$,f1�=  �/��   �T$,��  ����  �/��   �|$, �/��  �D$,��=����  �|$,���~1H��)$ H�H��X  H+�P  H��i��m۶���9D$,��  �D$,-  0=�����  �|$,�e�������  �|$,���H�n)$ H�8�,  H��'$ H�0����1�������    H�
  �� �/��
  H��$�   L�z$ H��H�
���H�"$ H�0����1��	���H�R$ H� ��|  �D$,����H�9$ H� ��p  �D$,�����H� $ H� ���  �D$,�����H��$   H�D$xH��$�   H�D$@H��$�   H�D$XH��$�   �T$,H�5� L��1�����L�t$@H��`   L������L��L�t$xH�� L������H��$  L��H��H��H�D$PI������H��$   H�i L��H��H�D$0I���~���L��$�   L��L��L���h���H��$   H�t$XH�x��B���H��$  L��H�x��.���H��$   L��H�x�����H��$�   L��H�x�����H��$8   tH��$8  �8 ��  H��$�   �]����D$,H��$�   H��m۶m۶mH�HH�PH��H)�H��H�ƅ��  H��$�   E1�E1�I��H�D$8H��$�   H�D$`H��$�   H�D$h�&�    H��A��I��8H)�H��I��A9���  N�4"I�F H�x� t�H�|$8I�F H��`   H�D$H�w���H�t$8H�|$X�����H�|$XH�5� �   ����H�t$XH�|$@�����H���{���H�|$@H��H�������H�t$@H�|$x����H�|$x�.   �   �X���L��L�t$PH�t$xL������L��L�t$0L���s���H�5t �   L��L�t$0����H�|$`H�T$HH�t$0I�������H��$   H�t$hH�x��M���H��$  H�t$0H�x��7���H��$   H�t$0H�x��!���H��$�   H�t$0H�x�����H��$�   H�t$0H�x������H��$�   H�t$0H�x������H��$�   �R������    ED$,H�t$0�D$,H��$�   H�x�����H��$�   H�PH�H�'���H��$�   H�t$0H�x������D$,����H�x H���  H�xH ��  �����H��$�   L��$p  1�H��H��$�   H�Q �e���L������H�
  H��8I9�u�J�<H��tL�\$L�$�����L�\$L�$I�H�x�H9��0  I��pL9L$�/���L�l$0I��  H��t����I���   H�\$(H�x�H������I���   H��H�x��n���I���   H��H�x��[���I���   H��H�x��H���I�}`H��t�J���I�}HH��t�<���I�}0H��t�.���H��h[]A\A]A^A_�L������H�D$_L��H�D$(����L������H�=�$  L�G��  ������A�0�������H�t$(L�\$HH�T$@H�D$8H�L$L�$����L�\$HH�T$@H�D$8H�L$L�$����H�=*$  L�G�  ������A�0���N���H�t$(L�\$HH�T$@H�D$8H�L$L�$�$���L�\$HH�T$@H�D$8H�L$L�$����H�=�
$  L�G��   ������A�0�������H�t$(L�\$HH�T$@H�D$8H�L$L�$�����L�\$HH�T$@H�D$8H�L$L�$����H�=d
$  L�Gty������A�0���{���H�t$(L�\$HH�T$@H�D$8H�L$L�$�b���L�\$HH�T$@H�D$8H�L$L�$�<���D�F�E�P�D�V�D�������D�F�E�P�D�V�D���l���D�F�E�P�D�V�D���D�F�E�P�D�V�D���
H��8H�y� u2H��H9�u�f�     L��H������I�V8I�N@������     I�wpH��������f�H��[]A\A]A^A_�H�
�     H�EPH��H�EPH�sH�C�D$fff?�D$ 33�?H9���  H���5  �33�?H�CH�sH��Ǆ$�       H9�H�C��  H��t�     H�CH�sH��Ǆ$�       H9�H�C��  H��t�     H�CH�sH��H9�H�C��  H��t�D$� H�CH�sH��H�C��l H9��YD$0��$�   �!  H��t� H�CH�sH��H�C��l H9��YD$P��$�   ��  H��t� H�CH�sH��H�CH9���  H��t�D$� H�CH�sH��H�C�Gl H9��YD$@��$�   �'  H��t� H�CH�sH��H�C�l H9��YD$`��$�   ��  H��t� H�CH�sH��H�CH9���  H��t�D$ � H�CH�sH��H�CH9�Ǆ$�       �9  H��t�     H�CH�sH��H�CH9�Ǆ$�       ��
  H��t�     H�CH�sH��H�CH9���
  H��t�D$� H�CH�sH��H�C�'k H9��YD$@��$�   �G
  H��t� H�CH�sH��H�C��j H9��YD$`��$�   ��	  H��t� H�CH�sH��H�CH9���	  H��t�D$ � H�CH�sH��H�CH9�Ǆ$�       �9	  H��t�     H�CH�sH��Ǆ$�       H9�H�C�:	  H��t
�     H�CH��H�CH�uH�EǄ$�   l�	?H9���  H���  �l�	?H�EH�uH��H�E��i H9��YD$0��$�   �  H��t� H�EH�uH��H�E��i H9��YD$P��$�   ��  H��t� H�EH�uH��H�EH9�Ǆ$�   l�	?��  H��t� l�	?H�EH�uH��H�E�[i H9��YD$0��$�   �G  H��t� H�EH�uH��H�E�"i H9��YD$P��$�   ��  H��t� H�EH�uH��H�EH9�Ǆ$�   l�	?��  H��t� l�	?H�EH�uH��H�E��h H9��YD$@��$�   �F  H��t� H�EH�uH��H�E��h H9��YD$`��$�   ��  H��t� H�EH�uH��H�EH9�Ǆ$�   l�	?��  H��t� l�	?H�EH�uH��H�E�h H9��YD$0��$�   �E  H��t� H�EH�uH��H�E��g H9��YD$P��$�   ��  H��t� H�EH�uH��H�EH9�Ǆ$�   l�	?��  H��t� l�	?H�EH�uH��H�E�xg H9��YD$@��$�   �D  H��t� H�EH�uH��H�E�?g H9��YD$`��$�   ��  H��t� H�EH�uH��H�EH9�Ǆ$�   l�	?��  H��t� l�	?H�EH�uH��H�E��f H9��YD$@��$�   �K  H��t� H�EH�uH��H�E��f H9��YD$`��$�   ��  H��t� H�EH��H�EH�s H�C(H9��|  H���c  �D$�H�C H�s(H��Ǆ$�       H9�H�C �u  H��t�     H�C H�s(H��Ǆ$�       H9�H�C �n  H��t�     H�C H�s(H��H9�H�C �j  H��t�D$� H�C H�s(H��H�C ��e H9��YD$@��$�   ��
  H��t� H�C H�s(H��H�C �re H9��YD$`��$�   �B
  H��t� H�C H�s(H��H�C H9�� 
  H��t�D$� H�C H�s(H��H�C �e H9��YD$0��$�   �y
�     H�E A��H��A��H�E ��	  �A*�H�|$H�t$�Yg� �^�b �b����d$�l$(��$�L$(��$$�l$H�s8�D$0H�C@�L$P�D$�̌��T$@�D$ fff?�\$`H9��a  H�������H��1������fD  H��1������fD  H��1�����fD  H��1��7���fD  H��1��"���fD  H��1������fD  H��1��~���fD  H��1�����fD  H�T$H�{����H�C H�s(Ǆ$�       H9������fD  H�{L������H�C H�s(Ǆ$�       H9������H�{L���Ĭ��H�C H�s(H9������ H�T$H�{袬��H�C H�s(����D  L��H��腬��H�CH�sǄ$�       H9������f�     L��H��H���R��������D  L��H���=���H�CH�s�S���L��H���%���H�CH�s�����     L��H������H�CH�s�����     H�T$H������H�CH�s�_���fD  L��H���ū��H�CH�s�����     L��H��襫��H�CH�s������     L��H��腫��H�CH�s�{����     L��H���e���H�CH�s�3����     L��H���E���H�CH�s������     H�T$H���#���H�CH�s����fD  L��H������H�CH�s�9����     H�T$0H�}0����H�E8H�u@H9�������H�T$PH�}0�ª��H�E8H�u@Ǆ$�       H9������fD  H�}0L��蔪��H�E8H�u@H9������ H�T$0H�}0�r���H�E8H�u@H9�������H�T$PH�}0�R���H�E8H�u@Ǆ$�       H9������fD  H�}0L���$���H�E8H�u@H9������ H�T$@H�}0����H�E8H�u@H9�������H�T$`H�}0����H�E8H�u@Ǆ$�       H9������fD  H�}0L��贩��H�E8H�u@H9������ H�T$0H�}0蒩��H�E8H�u@H9�� ����H�T$PH�}0�r���H�E8H�u@Ǆ$�       H9�����fD  H�}0L���D���H�E8H�u@H9��	��� H�T$@H�}0�"���H�E8H�u@H9������H�T$`H�}0����H�E8H�u@Ǆ$�       H9�����fD  H�}0L���Ԩ��H�E8H�u@H9����� H�T$@H�}0貨��H�E8H�u@H9������H�T$`H�}0H��菨��H�sPH�CXH9��'���fD  H�T$H�{H�j���H�CPH�sXǄ$�       H9��.���fD  H�{HL���<���H�CPH�sXǄ$�       H9��/���H�{HL������H�CPH�sXH9��6��� H�T$H�{H����H�CPH�sX�7���D  H�{HL��H���ѧ���&���@ H�{HL��輧��H�CPH�sX������    H�T$H�{H蚧��H�CPH�sX�y���D  H�{HL���|���H�CPH�sX�3����    H�{HL���\���H�CPH�sX������    H�}HL��H���9����=���@ H�}HL���$���H�EPH�uX������    H�}0L������H�E8H�u@������    H�{0L��H�������r���@ H�{0L���̦��H�C8H�s@� ����    H�{0L��謦��H�C8H�s@������    L��H��荦��H�EH�u�8���H�T$H�{�r���H�C H�s(����D  H�{L���T���H�C H�s(�����    H�{L���4���H�C H�s(�e����    L��H������H�EH�u������     H�}L�������H�E H�u(Ǆ$�       H9��4����     H�}L���ĥ��H�E H�u(Ǆ$�       H9��3����     H�}L��蔥��H�E H�u(Ǆ$�     ��H9��2����     H�}L���d���H�E H�u(Ǆ$�       H9��1����     H�}L���4���H�E H�u(Ǆ$�       H9��0����     H�}L������H�E H�u(Ǆ$�     ��H9��/����     H�}L���Ԥ��H�E H�u(Ǆ$�       H9��.����     H�}L��褤��H�E H�u(Ǆ$�       H9��-����     H�}L��H��A���m���A���-��� H�SH+H���������L�%�# �D$p    H��I�4$H��H��I�t$H��H��H��>  H�-'�# H��Ǆ$�       H�u H�u�;  H�S H+SI���������I�t$�D$p    H��I�t$ H��I��H��H��H���  H�uH��Ǆ$�       H�u ��  H�S8H+S0I���������I�t$0�D$p    H��I�t$8H��I��H��H��H��%  H�u0H��Ǆ$�       H�u8��  H�SPH+SHI���������I�t$H�D$p    H��I�t$PH��I��H��H��H���  H�uHH��Ǆ$�       H�uP�@  H�Đ   []A\A]A^�H�{L��H���Ѣ�������@ H�{L��輢��H�C H�s(�����    H�T$H�{0蚢��H�C8H�s@�����D  H�{0L���|���H�C8H�s@�����    H�{0L���\���H�C8H�s@�H����    H�T$H�{0�:���H�C8H�s@�����D  H�{0L������H�C8H�s@�����    H�{0L�������H�C8H�s@�����    H�T$H�{0�ڡ��H�C8H�s@�[���D  H�{0L��輡��H�C8H�s@�����    H�{0L��蜡��H�C8H�s@�����    H�{0L���|���H�C8H�s@�c����    H�{0L���\���H�C8H�s@�����    H�{0L���<���H�C8H�s@������    L��H������H�CH�sǄ$�       H9��^����L��H�������H�CH�sǄ$�       H9��e���f�     L��H���Š��H�CH�sH9��d���@ H�T$H��裠��H�CH�s�e���fD  H�}HL��脠��H�EPH�uXǄ$�       H9������     H�}HL���T���H�EPH�uXǄ$�       H9������     H�}HL���$���H�EPH�uXǄ$�     ��H9������     H�}HL�������H�EPH�uXǄ$�       H9������     H�}HL���ğ��H�EPH�uXǄ$�       H9��
��  H�\$(D��B��  9T$$|l�\$09\$tb��	t	�� �C  �L$��@��E��D$1�HcL$�$9��  H�\$���	u�H�t$(H���# ���  �3�9T$$�   }�D9�~�|$�L$09�DǉD$@���(  �T$#�D$L��D)������H�D$Hc�J�48�H���H�D$H�xH;x��  H����  L��蔓��H�D$H�@H�t$H��H�F�D$���|$# �|   ;$}?H�t$Hc����	��  H�t$H�T�fD  �
H����	�8  ��;$u�H�D$`H�\$8H�x�H���2���H�D$PH��H�x��!���H��x[]A\A]A^A_�f�Lc��D$�D$# 1��   1�Hc����� T$4�D$�D$#�M���fD  1�1�L�������D$#D�t$1�E)�L|$����A�E��_�����D  H�D$PH�P�L�bL;`�w�H���~L��L���{���H�D$PH�P�@�,H�D$PH�P�H;^�# ��   H��A9��s���A�,@��	u�H�t$L���s����ِ�D$�   HcL$�����@ �� �����Lc��D$1��   1��J���f.�     1��>���H��H�|$L��虗���6����� �Y�����@�    L�`�B�  �\���H�
   H�CH    �CP H���Cp    H�CX    H�H�C`    H�Ch    ��|��L�|$@�CP H��$0  I���   H\  H�C(I���   H��$0  H���   H�$�˂��I���   H�5e� ���  H�ߋ��  +��  �����I���   H�5~� ���  H�ߋ��  +��  ����I���   H�5G� ���  H�ߋ��  +��  �v���I���   H�5X� ���  H�ߋ��  +��  �O���I���   L��L��$�   H�xxL��H�$车��ƃ�    L���s��H���  []A\A]A^A_�H��  I��H���:���f�fZ����  �AX��   .�w�����H�xx ����������H���   J�<(H������
  A�D$8��  H���   J�(�@dA�D$xH���   J�(�@hA�D$|H���   J�(�@lA��$�   H���   J�(�@pA��$�   H���   J�(�@`A�D$aA�D$`�F���H�<$H�p�x�������H��$  H�\$hH�x�H���Z{��H�D$pǄ$8      H��HǄ$@      HǄ$X      H��$H  H��$P  H�D$@H�@0J�48H��x�9���H��$H  H;D$pH�D$X�����H��$�   Ǆ$�       1�H�D$xH��$�   H�D$H�D$X�@ �$��   �3���H���# H� H��  H��   H)�H��i��m۶9�����Hc�H��H��H��H�T$(H)D$(H�D$(H�H�x� �����H���  ��   �u��H��I����{��H�#�# I�D$@    A�   A�D$8��  A�D$!H�\$(L��$�   H� L��H���   H�R8���  fE�D$&A�D$`A�D$a�RfA�T$$H�  H�Ȼ# H��H��$�   H�H�p�H���q���{   �   L���:o��H��L����v��H�\$xH��$�   H���Zs���}   �   H���o��H�t$xI�|$�i��H��$�   H�\$0H�x�H���@y��H��$�   H��H�x��,y��L��$  H�u@H�E8H;uHH�D$8�w  H����  L�&H�E@H��H�E@H+E8H�L$(H�D$`H�з# H�|$`H� H�  H�q8H��H�~� �=  H�pH�PH��m۶m۶mH��H)�H��H��H����  H��$�   H�D$     L��$   H�D$H��$�   H�D$H�1�     H��H��m۶m۶mH�D$ H)�H��H��H9D$ �s  H�\$ H��H��H��H)�H�D0H�x� t���   �>}��H��I���y��H��# I�D$@    I��A�D$8��  A�D$x    A�D$|    AǄ$�       H� AǄ$�       A�D$` A�D$aH�L$(H��  LlI�EH�x� �N  H�
H��L�d0��  M����  H�@8H��H�D$�|  H�z�# H�
   H�EH    �EP H��H�EX    H�E`    H�E H�Eh    �Ep��  �4q��H�D$@�EP Ǆ$�       H���   H�$H  H�E(�G���H�T$0H����j��H�E@����1��|����������P��������L���?v������f.�     E1��>���H�yH���Kw���I���H��# A�D$8��  A�   A�D$!H� H���   H�R8���  fE�L$&�RfA�T$$H���   H�@8��  ��	��fA�D$&A�D$`����H��H��$�   �
A�> �A  H�L$P1�H��H���D$P �c�����D$LH�D$(    �_
�# H�8�Rg��H���# H�8�k��H��tH��# H�8�ok���8 ��  H�E� H��H�|$( M��L�ď �
��  ��1�1���D�?���fDfAfADH��9�r�D9������B��C��������������H�4�I��I9�@��H9���@���  ����  A��1�1�A��B�<�    �A���LAfDAfLAfZ�fZ��AADH��A9�w�9��}������4�fZ��A4��G9��a����<���9�fZ��A<��G����,�fZ��A,��3���H��$�   I�t$D��H���+b��L��$�   H��L��H��$�   L���T��I�~�H���^��H���# H��H��$�   H��$  H����  H��$   H��$�   �$ H9���   H��$  H��� f��H��H��H��$�   �]d��H��$�   H��$�   H�x��4^��H��$�   H��A��H�h�H�x��^��A9��H��$�   IF�H�l$8H�\$0A� �   ����H�ל# H�8�_f��H9D$@�����H���# H�8�Ef��H��� �����L�
�SHD������������p����2���L��H�T$H�L$L�D$��Z��H�T$H�L$L�D$����H��L)�H�H�x�L9�uH��@I9�u�������p�D�N���D�H��L��H�T$H�L$L�D$�qZ��H�T$H�L$L�D$�AWAVAUATUSH��   H�X�# L�(M����  ��I��I���D$H��A���Q  I��  H��t�RY��H��# L�(Iǅ      I��P  H��H�D$I��X  H��H�D$H��m۶m۶mH)�H��H��M��tA�<$ �C  H�-Ӗ# �D$`    H�D$h    HǄ$�       H�EH�D$PH�D$PH��H�D$H�D$`H�D$pH�D$xH�D$I;�`  �d  H����  H���N��I��X  H�D$hH��8I��X  I��M��trH�D$H��H�=�#  ��  H�L$@H�\$L�d$L��I��H�L$ �@ L��H�sL���O��H�K(L�cH�y�H9��2  H����I��M��u�L�d$H�\$H�D$PH�x�H9���  H���# H��m۶m۶mH�\$ H� H��P  H��X  H)�H��H���^H�������1�L�l$(H��I����    I��H��8I9���  H�u L���Q����u�L��L�l$ L�l$(H9��g���H�l$H��H9D$ �M  H�D$ H��    H��H)�H�M��tA�<$ �I  H�5f� 1�H���fR��H�EH�uH��H�D$�N���|$ H�EH�E    H�E0    H�E H�E(��   H�D$PH�L$>L��L�l$@L�t$XE1�H�D$H�D$?H�L$H�D$(@ H�sH�� H�T$L��H��HD��yU���L��L���D$P�8L��H�D$@H�-L�# H�x�H9���   H�t$H�|$�0P����uH�x(L���0X��H�D$XH�x�H9���   A��H��E9��u����D$     �"�|$ �����H���# L��H�0�V��1� H�Ę   []A\A]A^A_�L���FK��L��H��H���Q������L�l$(����H�z�# H�8��T��1��1�1�����H�=�#  H�W��   ����������L���L���2V���?���H�=�#  H�W��  ��������������H�t$(� V�������H��H�t$I��P  �W��L�l$h����H��H�D$XL��H�x���P��H���Y��H�
H����������!�%����t��H�t$H�������  D�H�JHD� �H��H)��B������D  H�!�# H�H��   H;�(  ��  H����  H�t$8��I��H�
f ��$�   ��$�   H��$�  H��$   H��e H��$  H��$8  L�\$0H��$   H�Ee L�T$8H��$�   H��$�   H��$  H��e H��$8  �y  H��e L�
� @ H��I��I� I�PH��H�H�Wu�fnL$fnT$fnD$fn\$fb�fb�fl��H�� []A\A]A^��    1��b���H�H)�H��H����   I��A�   I)�I��L��H�T$H�t$�$��H�t$H�T$I��H�@I��M�tL�
L�RM�M�VH�H9���   H��L��f.�     H��tL�L�PL�
L�RH��H��H9�u�H��H��H)�H���I�l H�CH9�t<H��H��fD  H��tL�	L�QL�
L�RH��H��H9�u�H��H)�H���H�lH�;H��t�;��M�L�+H�kL�c�����L�	L9�vI��I������I)�I�������H���t���H��������I9�w�I��I��I)�I��I��M��������   E1������f.�     �AWAVAUATUH��SH��8H��H�t$(�  H�D$(H�@H��H�D$��  H�@H��H�D$��  H�@H��H�D$�N  H�@H��H�D$ �	  L�xM����   M�oM����   M�eM��te�M�t$M��u	�2@ I��I�vH�����I�~(I�^H��t��
H�\$(�����H��8[]A\A]A^A_� H�GH;Gt&H��t�� H�GH��H�G�@ 1���@ H��H��H������H���f.�     f�AVAUATUH��SH��H��H�GH;Gt\H��tOH�H�H�H�H�GH�CH�H�W�H)�H��H��tH��H��H)����H�] H��[]A\A]A^�f�     1��@ H�H)�H��H����   H�4 H9���   I��I������I)�I��L��H�T$���H�T$I��K��H��tH�H�H�3H��E1�H)�H��H��tL�$�    L��L�����H�KO�D&E1�H)�H��H��tL�$�    L��H��L������I��H�;M�H��t��
��M�L�3L�cL�k�����    I��A�   I)�I���D���H��������H9�� ���I��I��E1�I)�I��I��H���)�������fD  AVAUATUH��SH��H��H�GH;GtTH��tG�H��H�H�GH�C�H�W�H)�H��H��tH��H��H)�����] H��[]A\A]A^�D  1��@ H�H)�H��H����   H�4 H9���   I��I������I)�I��L��H�T$����H�T$I��K��H��t��H�3H��E1�H)�H��H��tL�$�    L��L�����H�KO�D&E1�H)�H��H��tL�$�    L��H��L���W��I��H�;M�H��t�T	��M�L�3L�cL�k�����I��A�   I)�I���L���H��������?H9��(���I��E1�L�,�    I)�I��H���0�������D  AWAVAUA��ATA��UH��SH��H��8L�WL;W H�t$ H�T$(�  �GA�   ��H�rH���?H��M��L��H)�H�<�D��H)�H��:�zf.�     ��M�ى�I���t:��L�ډ�H��M�
uBH��I#H��I�tB��u�I����I�       ��?   u�I��M�
H�       ��?   t�IH��I�u� �   D��H��E��u0H��H#E H�E �C�P��?�St'H��8[]A\A]A^A_��     HE ��f.�     �C    H�CH��8[]A\A]A^A_�L+�G�WJ��H)�H��������H9��  H��I��H�D$   tH� H9���  H��������H�D$H�|$E1��h��H�3H�D$H��H)�H��H���u  H�D$N�8D��H��H�D$��  H��I�2I��1�1�A�   �7fD  L��H��H!΃�?I�2D�JtM��?�Ht.M�ǍP��I��H��t=D�ʉ�M��M��I���I��M�;t�L	��I�rA�   I���   1���I��E1�몃�?L����   L��L��H��H	�H!�E��HD�I�H�C�KH)�H�4�H+t$H��~bA�   �/�     H��H#A��?H�A�L$t6��?�Bt&H��t2��A�̉�L��M��H��D��I��M�>t�H��H��1���I��1���H�{H�;�CH��t����H�|$H�D$�C    H�H�;H�C �����L�<�    H�|$L������q���I�z1�����I�2L��A�   �   �����H�=�= ����H9�H��HF�H��?H��H��H�D$������H�GH;Gt&H��tH�H�H�GH��H�G�fD  1���@ H��H��H���a��H���f.�     f�AWAVI��AUL�oATUSH��H�_L�%�P# H��tHH�=kK#  L�|$u�jf.�     H��H�sL���A
��H�C(H�kH�x�L9�u(H�����H��u�I�H�x�L9�uVH��[]A\A]A^A_ú������P����L���2����H�sL����	��H�C(H�kH�x�L9�u9H���;��H��t�H����H�=�J#  H�Wt0����������H�t$����냋P��J��҉H��L������밋P��J��H�����fD  AWE1�AVI��AUI��ATUS1�H�����I�FI+FH��m۶m۶mI�E    I�E    I�E    H��H��H����   L�M�}M�}I�]M�fL��I�nI9�tb�     H��tHH��H��� ��H�uH�{����EH�u H�{ �CH�EH�C����H�E(H�u0H�{0H�C(����H��8H��8I9�u�I�]I�F I�v8I�}8I�E I�F(I�E(I�F0I�E0���A�F@A�E@I�FHI�EHI�FPI�EPI�FXI�EXI�F`I�E`I�FhI�EhH��[]A\A]A^A_�D  H��$I�$I�H9�w"H��    H��H��H)�H�����I����������H��I�} H�t$H���=��H���U��I�}H���i
�%D  M��I�uH�������M�uL�������M��u�L�mH������M��tL���I�l$L������H��tI���I�oL������H��tI���s���H�D$ H�hH���i���H��t
H�l$(�m���H��8[]A\A]A^A_�f.�     D  AWAVAUATUSH��H��8H��H�t$(��  H�D$(H�@H��H�D$�\  H�@H��H�D$�'  H�@H��H�D$��   H�@H��H�D$ ��   L�xM����   M�gM��tkI�l$H��tJ@ L�mM��u
�%D  M��I�uH������M�uL�������M��u�L�mH�������M��tL���I�l$L�������H��tI���I�oL������H��tI���s���H�D$ H�hH������H��t
H�l$(�m���H��8[]A\A]A^A_�f.�     D  ATI��USH�_H��u�&fD  H��H�sL�������H�kH������H��u�[]A\� AWAVAUATUH��SH��H���L  L�gH�GI��H��L)�H��H9��@  L��D�)H)�H��H9��Z  H)�L���   ��H��H��H�ڃ�H9�HG�H����  H��E�,$H�u�I�T$v^H��E�l$H�u�I�T$vJH��E�l$H�u�I�T$v6H��E�l$H�u�I�T$v"H��E�l$H�u�I�T$vH�u�I�T$E�l$H9�teI��I)�M��I��N��    M��t4D�l$I�<�1�fnL$fp� H��H��fG�L9�r�L)�M9�J��tH��D�*tH��D�jtD�jI�VH�<�1�H��I�~��  H�I9�I�n��   H�CH��L���H)�H��H��H��H����H9�H��HG�H����  H��D�+v@H��D�kH�Cv2H��D�kH�Cv$H��D�kH�CvH��D�kH�CvH�CD�kH9�teH)�H��H��L��    M��t1D�l$H��1�fnT$fp� H��H��fA�H9�r�L9�J��tH�PD�(I9�tH�PD�hI9�tD�hH��[]A\A]A^A_� H�H��������?H��I)�I��L)�H9���  I9�H��I��IC�I)�I��I��p  I������L��H�L$����H�L$I��K�T� �1I��H�у�H��H�ك�H9�HG�H���  I���2H�E�H�JvOI���rH�E�H�Jv>I���rH�E�H�Jv-I���rH�E�H�JvI���rH�E�H�JvH�E�H�J�rL9�taI��M)�M��I��N��    M��t3�t$J�<�1�fn\$fp� H��H��fG�L9�r�L)�M9�J��tH���1tH���qt�qI�6H��E1�H)�H��H��tL�<�    L��L�����I�FM�I��1�H)�H��H��tH�,�    H��H��H���u��H��I�>H�H��t�r���M�M�.I�nM�fH��[]A\A]A^A_�D  H�,�    H��H���-��I�~����@ H����  1�H��L���k���f.�     H����  1�H���^���D  H��M��L��H��I)�H��H��tH��L��L������I�FL��H�H)�I�FH��H��tH��    L��H��H)����H�H9��Z���H�CH��H���H)�H��H��H��H����H9�H��HF�H���  H��D�+v@H��D�kH�Cv2H��D�kH�Cv$H��D�kH�CvH��D�kH�CvH�CD�kH9������H)�I��I��J�<�    H��t5D�l$H��1�fnd$fp� H��H��fA�I9�w�H9�H�������H�PD�(H9��v���H�PD�hH9��a����`���f.�     H��uBE1�H��H���@���D  I9������M��uME1�E1������    H��u1�H���=���I������H�������H������H�=�� ���H���0���I���3���@ AWAVAUATI��USH��H����  H�oH�GI��H��H)�H��H9���  H��L�1H)�H��H9���  I)�H����   H��<H��?L9�H��L��IG�I���g  H��L�u H�uv@H��L�uH�uv2H��L�uH�uv$H��L�uH�u vH��L�u H�u(vH�u0L�u(I9�tHM��I)�L��H��I��M�t1L�t$H�L� 1��~D$fl�H��H��fA�H9�r�M9�J�4�tL�6I�UJ�<�E1�H��I�}��  I�H9�M�e��   H�KH��H��<H)�H��?H��H��H9�HG�H��H��H����  H��L�3v@H��L�sH�Kv2H��L�sH�Kv$H��L�sH�K vH��L�s H�K(vH�K0L�s(H9�tDH)�H��H��H��H�t0L�t$H��1��~D$fl�H��H��fB�H9�r�H9�H��tL�1H��[]A\A]A^A_�f�H�H��������H��H)�H��H)�I9��t  L9�L��I��HC�I)�I��H��  H������H��H�L$�����H�L$I��K��H�H��H��<H��?L9�H��L��IG�I����  H��H�H�rv@H��H�BH�rv2H��H�BH�rv$H��H�BH�r vH��H�B H�r(vH�r0H�B(I9�tGM��I)�L��H��I��M�t0H�D$H��1��~D$fl�H��H��fA�H9�r�M9�J�4�tH�I�u H��E1�H)�H��H��tL�<�    L��L������I�MM�O��E1�H)�H��H��tL�$�    L��H��L���y���I��I�} M�H��t�u���L�M�u M�eI�mH��[]A\A]A^A_��    L�$�    H��L���-���I�}�P���@ H����  1�H�������D  H����  1�H������D  I��I��H��L��M)�H��H��tH��L��H�������I�EL��L�H)�I�EH��H��tH��    H��H��H)�����I�I9������H�KH��H��<I)�H��?I��I�T$H9�HF�H��H��H����   H��L�3v@H��L�sH�Kv2H��L�sH�Kv$H��L�sH�K vH��L�s H�K(vH�K0L�s(H9�����H��H)�H��H��I��M������L�t$H��1��~D$fl�H��H��fB�H9�w�L9�J������������    H��u:1�H�������H9������H��uM1�E1�������     H��u1�H���i���H�������H������H�������H�=4� �����H������H������f.�     f�AWI��AVAUATI��USH��HH9��|  L�nH�H��m۶m۶mM�4$L��H)�H��H��H��H��H�$H��I�D$L)�H��H��H9��z  I�D$H�D$L)�H��H��H9$�+  H�<$ ~qM���H��L������H�sI�}�����CH�s I�} A�EH�CI�E�|���H�C(H�s0I�}0H��8I��8I�E��_���H��u�H�$H��    H��H)�I�H�T$L9�twH�"8# I�N0I�F M�~L�D$?L��fD  I��M)�I�t
H�|$��������H���{���H�D$ HD$H�\$@H�t$M��H��H�,L9�t�I�G8H��I��@H�x��C�����H�������H���Q���L�t$?H��H�D$xL��H�x�����H���0���L�t$?H��L��� ���H9�tLJ�D- L��H��@I�|8H���������H�\$@H��M�������I9�t K�/H��I��@I�|8H������������������H���]����o���H���P���H�������     AVI��AUI��ATUSH��H9���   H�jH��I�� H����   L��H���$���H�S�E     H�E    H�E     H�{H�UH�UI�t$H��tD����H�CH���D  H��H�QH��u�H�K �	fD  H��H�HH��u�H�C(I�D$0H�C0I��8H��8H��8M9��b���H��H��[]A\A]A^�H����H������I9�tL��I��8�����I9�u�����H��H�H�t$H�x��n���H����H���!���H���y���f�     AWAVAUATI��USH��H��   H�oH;oH�t$�  H���  H�u�H�������H�u�H�UH�}�E    H�E    H�E0    H�U H��H�U(tA�����H�EH��� H��H�QH��u�H�M �	fD  H��H�PH��u�H�E(H�E�H�E0I�D$H��8H��I�D$H�D$@H��I��H�D$ �Z���H�sI�W�D$P    H�D$X    H�D$p    H�T$`H�T$hH��tKI��P���H�D$XH���	fD  H��H�QH��u�H�L$`�D  H��H�PH��u�H�D$hH�C0H�D$pM�|$I�G�I�o�H�D$H+D$H�D$H�|$H��m۶m۶mH�D$H��H�D$��   fD  H��H��8H��H������H�sL�cI��M)�L���0���H�D$H�SH�C    H�S H�S(L�pKǄ5�       H�s�H��tKL���f���H�CH���D  H��H�QH��u�H�K �	fD  H��H�PH��u�H�C(K�D/�K��5�   H�l$�O���H�t$ H�|$����H�D$H�XH�D$ H��H9���   L�t$H��I�v�l���H�t$XI�VI�F    I�F0    I�V I�V(H����   H������I�FH����H��H�QH��u�H�|$H�O �f�     H��H�PH��u�H�|$H�G(H�D$pH�G0H�t$XH�|$ H�������H�D$@H�t$?H�x��,���H�Ĉ   []A\A]A^A_�1��x���1���H�H��m۶m۶mH)�H��H��H���K  H�D- H9���  H�l$H��m۶m۶mH������H)�H��H��H��$I�$I�H�D$(�����H�D$H��    H��H)�Hl$H�l$ t}H��H���`���H�U�E    H�E    H�E0    H�}H�U H�U(H�sH��tB�[���H�EH���H��H�QH��u�H�|$ H�O �H��H�PH��u�H�|$ H�G(H�C0H�G0H�\$H�T$I�<$H���(���I��L�h8I�D$H9�H�D$��  L�|$I�nHL��f�H����   L��H������H�S�E     H�E    H�E     H�{H�UH�UI�wH��tK����H�CH���	fD  H��H�QH��u�H�K �	fD  H��H�PH��u�H�C(I�W0H��L)�I�T�H��8I��8H��8L9|$�Z���I�l$M�,$H�D$@H��H�D$ I9�t3�    I�uI�}I��8����I�E�H�t$ H�x������I9�u�I�<$H��t�����H�D$(H�|$I�\$H��    H��I�<$H)�H�I�D$�w���H�l$�   H)�H��H��H��    H��H�D$(H��H)������H��H�H�t$@H�x��^���H��H��L���P���I9�tH��H��8����H9�u��U���D  ��H�l$L�������H������H�|$ �_���H�|$ t
H�|$�
H�|$(��������H�������H�\$ L�Hk�8H\$(H;\$8t�H�|$8�B���H�D$88��H��肻��H�������H���r���H��H�\$(�a���D  AWAVI��AUI��ATUSH��8H9�H�t$H�T$�u  f�     M���P  L��L���<���I�EI+EH��m۶m۶mH�T$1�I�F    H�D$    H��L�zH��L��H)�H�$J�D9    J�D9    H���  H�D$H�$H�J�:J�D:J�\:M�eH��I�mI9�ta�    H��tHH��H��蠼��H�uH�{蓼���EH�u H�{ �CH�EH�C�x���H�E(H�u0H�{0H�C(�c���H��8H��8I9�u�H�$I�u8I�~8J�\8I�E I�F I�E(I�F(I�E0I�F0�(���A�E@A�F@I�EHI�FHI�EPI�FPI�EXI�FXI�E`I�F`I�EhI�FhI��pI��pL9l$�����H��8L��[]A\A]A^A_�f�H��$I�$I�H9�whH��    H��H��H)�H�������H�D$����H������L9t$tH�|$�T���H�D$pL9t$u�����H��I�H�t$.H�x��~���H��������I�~H��������H������H���v�����I��H�C H�l$/H��H�x��<���H�CH��H�x��,���H�H��H�x�����L��H������H9\$tH�|$�q���H�D$8H9\$u�����I��H�l$/�I��H�l$/��H��葸��I�~H���5����϶���+���f.�     AWH��AVI��AUI��ATUSH���   H�|$H�H;x��  H��1�H��t
  I�4H�~�H9���  H�u H�~�H9��>  H��8I9�u�H��$�   H��t����H��$�   L��H�x�����H���   []A\A]A^A_�D  H��$I�$I�H9��  H��    H��H)�H�������H�D$M�eI�m����L���H� H��m۶m۶mH)�H��H��H����  H�?H��H)�H��H9��  H��m۶m۶mH������H��H�I�$I�$IH�D$H�^���H�D$8H��H��H��H)�H\$8tL��H�������L�|$H�T$8L��I�?�d���H�hpI�wL��H���Q���H�D$PH�D$H�HH� H9�H�L$(H�D$ ��  H�P8H��L�XH���" H��$�   H�T$0H�D$@f�     I��L+D$ H�D$0I� H�x�H9��O  O�tO�,M9��   I�U0I�E M�}L��I��M)�I�4H�~�H9���  I�4H�~�H9���  K�4<H�~�H9��  H�u H�~�H9��&  H��8I9�u�K�<H��tL�\$H�L$�����L�\$H�L$H�H�x�H9���  H��pH9L$(�,���H�D$H�8H��t躰��H�D$H�t$8H�T$PH�0H��H�PH�D$HH��H��H��H)�H�H�A����H��   H)�H��H��H��H��H�T$HH��H��H)�����@ L���?���H�|$(�z���H�=��"  L�G��  ������A�0�������L��H�D$H�L$H�T$�Ӿ��H�D$H�L$H�T$�����    H�=x�"  L�G�4  ������A�0�������L��H�D$H�L$H�T$�}���H�D$H�L$H�T$�s���f�     H�= �"  L�G��  ������A�0���5���L��H�D$H�L$H�T$�%���H�D$H�L$H�T$�
����H�=��"  L�G��  ������A�0�������L��H�D$H�L$H�T$�ս��H�D$H�L$H�T$�����H�=��"  L�O��  ������A�1�������H�t$@L�\$hH�T$`H�D$XL�D$H�L$�y���L�\$hH�T$`H�D$XL�D$H�L$�u��� H�=�"  L�O�X  ������A�1���,���H�t$@L�\$hH�T$`H�D$XL�D$H�L$����L�\$hH�T$`H�D$XL�D$H�L$����� H�=��"  L�O�  ������A�1�������H�t$@L�\$hH�T$`H�D$XL�D$H�L$詼��L�\$hH�T$`H�D$XL�D$H�L$���� H�=H�"  L�Otg������A�1���O���H�t$@L�\$hH�T$`H�D$XL�D$H�L$�E���L�\$hH�T$`H�D$XL�D$H�L$����D�F�E�H�D�N�D���!���D�N�E�Q�D�V�D���D�F�E�H�D�N�D�������D�N�E�Q�D�V�D������D�N�E�Q�D�V�D���"���D�N�E�Q�D�V�D�������H�=r�"  H�WtS��������������H�t$@L�\$H�L$����L�\$H�L$�����H��船��H�|$8 t4H�|$8膬��葹���P��r��p����H������H���t���H���L���H�������H�|$8 u���H�I�$I�$IH9������H��H�������H�D$H    H�D$8    �����L�l$H��H�CL��H�x�����H�L��H�x��ܵ��H��H���Ѹ��H9\$H�l$tH��H��8�)���H9�u��ϸ��H��L�l$�������H���V���H��$�   H��t蔫��H��$�   H�t$~H�x��n���H��膽��I�H��蚶����H��H�C L�l$L��H�x��@����@���H��L������H���H���H���H�=��"  H�Wtv��������������H�t$@L�\$XH�L$L�D$�˹��L�\$XH�L$L�D$�e���H���Ϸ��H���z���H�D$8H9�H���q���H��H��p�Y���H9�u��[����P��r��p����D�F�E�H�D�N�D�������D�F�E�H�D�N�D������f.�     H���" H��" H��   �f�     �   �f.�     AUH�=�� ATUSH��H���" H���" H�����H�-��" H�=�f H�E �L�%�" H�=�f I�$�L�-B�" H�=�f I�E �H�}  H��H�&�" H�t)I�<$ t"I�}  tH�Ҹ   tH��[]A\A]�fD  H���" H�=jf �=   �   H��й��H��1�[]A\A]� �   �f.�     1�������t��t/��t1���    �   ��餱��@ �   ��锱��@ �   ��鄱��@ ��������t-��w �   H��0u\���   ��  @ 1��D  ��vSH�<�" �0@���t
�G�=�   v���   ~ҍ�������wW���/����    H��" � ����1���     �   H��0u|���   �t�H���" �0���0��     ���  ��t��������	�~   ��  �ȃ�=�D���H�5e Hc�H���@ H�y�" � ?�������fD  H�a�" �0��   �0����f�H�I�" � �������H�9�" �0@����0�����@ ���   �	����
v# ���xt�x��=�u# �t���@ H��H���#  �<�  H��a ��Hc�H���D  �w��Ť��1�H���fD  �G�   @��   ��������	��   H�
 u/�5*u# @���t	����   ��   t
@��?�5	u# ���������t#     H��Í�����- ��?��vjf�1�H���f�     �wf�� u�5�t# 1�������     ��H���d���@ ��t# ���xt�x��=yt# H�������   �w��H���)����  ��  ��  �ܸ  �ո  �θ  �Ǹ  ���  빍z`�����#���@ H��H���[  ���
�P  H�=` ��Hc�H���fD  �w�H��������w��Ţ��1�H���fD  1��=�s#  u-�W��t&���u!�����5  �5�s# ��`@����S��� ��s#  H���@ �
 ��s#  ��s#     ��   �{s#    ��   ��  �  A�   � ��   �5Vs# ��A�   �	 t�Bs# �ƋO�� �����&�9  H�o_ Hc�H���fD  �=�r# �=�r# H���@���1҃��G���L  ���3  ��t_�    1�H��Ð��r#  ��r#     1�H����     ��   ��  �   E1��3���f�     D��E1��7���D  �   ��H�������    �����lr# �d����
�   �C�  4�    �M         �1 ,�  @� )�  D�   +�                /�          �a        E�            4�           ?������ *�E�    p��Pa�  ��             �@      u�    f���J    4�                             �  %�%� B�    4�       ~�   0���   �Z4�  4��� !�j���     }�  �x�� (�  E�   4�     4�     
   3�                            �s�   I7���    �/a Ӯ��           �A      t�              4�    4�             ,�    	�:     ���g   :�  i�/     e  ��     4�  F ��  ��
�p 4� ��  ��
�p 4� �� �w8?    4�  4�  4� 0�  ^� �k    �  ���e J� 4�!     ��   4�    ��        4�   �j��          ��   X�   �y�i  4�  ?�  �V     4�  }� 4�      4�    �T     4�  4�   4�       4� 4���   4�    4��Bs�� 4��� 4� �6  i� 4�  <� �6  i� 4�  ;� .�        4�  4�  4�  �W  �� �� � �  �f��   �� �      e�   4�    0�       4�  �z �p         �CH��� �3��  4� �� �     4�  E� 4�      4�     �     4�  4�   4�       4� 4���    4�    4�=��B� 4�%�4� &�  @� 4� �� &�  @� 4� �� �H      4�  4�  4�  �� �I v��� �   >��    �|�     �D    4�     �v       4�  ,e   y         � �f�� V�;� 4�����  0�     4�  7� 4�����  4����� 0� ��� 4������   4�       4� 4���d    4�    4�Ѷ4� 4��x4� 1�  6� 4���� 1�  6� 4�����  &����%    4�  4�  4�  F� 3� N�S�� �   ��     |�K      ��     4�     @�      4�                  �6�
�l  ��   4�    ��       4�      �T     4�                  ��     ��   � 4����,   c��� 4���E  4�����c  4�      m���t 4�  4�  �����  ����2  4�  R�  4����� 4�  4� 4� ��  ��� 4�      ���� 4�   � 	����!    4�   3���   ��    �� �c ��  �   4�    ������   4�       `�     4�                  إ                                                                                                                                      W�9                                                                           4�       �D    4�                   ���                                                                                                                                       :                                                                           2��{       GO   ���                                                                                                                                                                                                                                                                          ������c                                                                                                                                                                                                                                                                                                   	|                                                                                                                                                                                                                                                          �   p�                                                                                                                                                                                                                                                         �    �b           4�                  4�           ���f         4�        1�     1�   4�    ���                                                             4�                                                       ���    4�  ��w                 D�                 4�                  4�           .�           4�                      4�      4�                                                             4�                                                      %�.     4�    `�                 �Q          g���$ 4�z��	  ����(  ��߅�  
  ��ނ�  4�N���   m���r ������ 4� 4� 3�	  I� �?    � 
          ]4	�� 4��	Ɛ  �� 5+  ����  ����   4�    ��
�� 4�y��     4�     4�   4���   4�   �3c�7`� 4�y��   ƞƕ 4��	Ƒ  ����  4��4  �> 9   4�   4� 4�  �T  �� �v    �  1�$h�	  �j  ��     ��     6�     4�    2�           _�d 7�               =� 4�!  Q� �)     �   R� �1  F�   4�   �#  R� 4� 8�    4�     4�   4���    4�   �8�8� 4� 8� �   Q� 4�!  Q� �   Q�  4�)     �`     4�   4� 4�  }� �0 j� �v�   j��(   g� �C    Z�
�� 4� 4�    4�     4�   4� ��   �C   �4�4� 8� 4�  ȞƗ 4��Ò  ��  4�     H|�   �3   �> ��   l��    ��*x�j  g�7�'   F��$   ��        9�     4�    1�             N�k            r���l� 4�x��	  ����)  ��߄�  ����o   4�    ���{� 4� 4�  v�����    4�   4� �    y��r �4�4� 8� 4�  ��� 4�x��
  ��߂�  4�     #����=    ���  x꿉�   ��    z� -�. 0�2  v�   ��    �����     4�     4�    4�             
�                                                                H�                    8�                                          4�          4�                                                            �i              2�     4�    5�                                                                           57��                    y�                                           4�          4�                                                           @�              �8     4�    j�                                                                             K����                  ���D                                           4�          4�                                                          ��\                ���    4�  ��`                                                                                                                                      �:l�                            f
:6                                                                                                                                                                                                             a;             !��                            <·                                                                                                                                   0�4�                                                                    :��                                                                                                                                  ���wM'�   I��                          *��           ����,                     9��                   4�     4�    �^�  m��I     /����I          �����          ������                    o�      4�    o� o�  4�4�                           ��(�   � ğ��    }�>                            ��~   ��  І  �� 3'                     �g                   4�     4�           �3e�    �w8?          ��4�             ��                    �E      >�    �^ �E  >�B�                           �'�~   � �7v�                                   }   �' U� �9                        �                  v�����H v�����H         �2d�9a .�            
�3 4�             e�                   #�      v�    #�#�  x� ��    T��L                           � �  �   m���r         g��z��          �����   x��  �����                     J�                     4�     4�           o��Ҕ\ 
�H      7�   (� 4�            �D                    4�      �?    4�4�  �? �?    ����                                   �> 9    �6   �>q�9j             ��   և�  3�                      ����                   4�     4�           ��Y
�3 4�          �K                                                                                                ~�    t�  '�<�           
x�                                                                                                  4�        4�8�            !A                              ����    �&     ����                                     4�                            4�  ����=                                                                                                  4�        tt            �                               4�    ����      4�                                                                                                                                                                                 4�                       tǲ                                                                                                      3Z       w    uT    ľ�            ��f                     c,       0_     )�&             a#       9U     .                    ٵ�    J�      �(    ��   ���                           e�      �=    *��             ��                     �S      �i     �[�T   "�?�e   0�4�    �`�                     '�     �#    �>�   0�4�    *�     �    �B�   0�4�           JnM�m     ��      �\    �?k�   %�<�{   0�0�                    ��     �h    �5]�   0�0�   *�                                                            �d�                                                                                                                                                         	                                                           ��     ��     ��     ��     P�      >�     �����   `��� 4�����D 4�����D 4�����D 4�����   �����   �����   �����   �����  4���D  4��  4�  ���  ���  ���  ���  ���          
�p  ��
�p  ��
�p  ��
�p  ��
�p          ��*� 4�  4� 4�  4� 4�  4� 4�  4� J� 4�! 4�     �Wx�    �y�i    �y�i    �y�i    �y�i   �a�     �k�    V�4�   �T     4�     4�     4�     4�       4�     4�     4�     4�   4�  }� 4��� 4� �6  i� �6  i� �6  i� �6  i� �6  i�  {  BQ �9 4�� 4�  4� 4�  4� 4�  4� 4�  4�  �� �  4����& 2�E��   �3��   �3��   �3��   �3��   @��   ��   �}4�  �     4�     4�     4�     4�       4�     4�     4�     4�   4�  E� 4�%�4� &�  @� &�  @� &�  @� &�  @� &�  @�  ��I�@ &��l� 4�  4� 4�  4� 4�  4� 4�  4�  �|�  4� �� 4��$    V�;�  V�;�  V�;�  V�;�  �� �`   u� �D  �=4��� 0�     4����� 4����� 4����� 4����    4�     4�     4�     4�   ���� 7� 4��x4� 1�  6� 1�  6� 1�  6� 1�  6� 1�  6�   |��<  1��e5� 4�  4� 4�  4� 4�  4� 4�  4�   |�K   4�  ;� 4��m   �� �\  �� �\  �� �\  �� �\  �i ��   ҂ ��  1�4�  �     4�     4�     4�     4�       4�     4�     4�     4�   4�  E� 4�4�:� &�  @� &�  @� &�  @� &�  @� &�  @�   P��  (�\� ?� 3�  4� 3�  4� 3�  4� 3�  4�   4�   4� �� 4�/��. ������ ������ ������ ������ 0�����
 1����� z����   �X     4�     4�     4�     4�       4�     4�     4�     4�   4�  ~� 4� ��� �6  h� �6  h� �6  h� �6  h� �6  h�  W�3q�" 
�� d� (�	  9� (�	  9� (�	  9� (�  8�   4�   4����& 4�  t� J�  W� J�  W� J�  W� J�  W� ��  �P ��  �^ �m 4�   f�>"c 4�     4�     4�     4�       4�     4�    :�    4�   4�R�C 4� K��  ��	�r  ��	�r  ��	�r  ��	�r  ��	�r  �1  lk 
�� �u
�� �u
�� �k	��   4�   4�     4�"v� ��   � ��   � ��   � ��   � �~   �� �^   �� � 4���   c���� 4�����c 4�����c 4�����c 4�����  �����   �����   �����   �����  4���E  4� ��  ���  ���  ���  ���  ���         �c���  3����  3����  3����  3���   4�   4�     4�t���J                                                             kx                                                                                                                                                                                                                                                            
��            o�       2�    ��                           �r       ��   ��                                   D�	       ,�    X�               @[                    �C      .�    s�{�   ��˽   �1�   i��n                    �i      y�     �JZ�    �1�    �z      �8    &�M�   1�5�   ��ry  V���%    �a      ��    �6\�   ��Ǯ  1�1�                   {�      �;    �N�    1�5�     3�  4�      1�5�      i      [.    qo                                                                            u     9H     J& ]          3n]�$                                                                   m     #P     1@ `             g   4�              g���$  g���$  g���$  g���$  g���$  g���$  �����  ����(  
 ]�   �W  ]4	��  ]4
��  ]4	��  ]4	��  ]4	��  ]4	��  0x�9h  �� 5+  ����  ����  ����  ����    4�     4�     4�     �    ���{ 4�y��   ƞƕ  ƞƕ  ƞƕ  ƞƕ  ƞƕ          ơ	� 4� 4� 4� 4� 4� 4� 4� 4�  �j  �� 4��	Ƒ �$ 0�
      =�      >�      =�      =�      =�      9�    7�6 �)     �2  C� �2  C� �1  F� �%  F�    4�     4�     4�     �   �#  V� 4� 8� �   Q� �   Q� �   Q� �   Q� �   Q�  ������ ���� 4� 4� 4� 4� 4� 4� 4� 4�  g� �C 4�!  Q�  �u ��   N�����  G�����  N�����  N�����  N�����  N�����  }����� 2�     /������ /������ /������ /������    4�     4�     4�     �   0�  8� 4� 4� 0�  7� 0�  7� 0�  7� 0�  7� 0�  7�         0��o6� 4� 4� 4� 4� 4� 4� 4� 4�  �$F� 4�  7�  D� �L  �_ >� �R A� �V A� �V A� �V A� �V A�  �95�  �)     �     �     �     �        4�     4�     4�     �   �"  S� 4� 4� �   Q� �   Q� �   Q� �   Q� �   Q�    0�  ��� Q� 0� B� 0� B� 0� B� 0� B�   ����  4�   P�  �>�  !�& �� !�E �� !�D �� !�D �� !�D �� !�D �� &�'_�L  �� 2*  ��  L  ��  L  ��  L  �� O    4�     4�     4�     �    £ʜ 4� 4�  ȞƗ  ȞƗ  ȞƗ  ȞƗ  ȞƗ          ��Ɨ �> �� �> �� �> �� �> ��   F��$  4��Ò   �ם    r�Ľ�  r���k�  r���l�  r���l�  r���l�  q���j�  ���_��  ����*  ����o  ����o  ����o  ����x  v�����  l�����  l����� �����"  ���
   +�H                                                               �                                                                                                                                  (                                         �i   4�       �                                                             
�C                                                                                                                                                                            @�   4�       ��                                                              @��                                                                                                                                                                           ��\    4�     ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  ;�}��             �}                          ;�Y  ��Y��     �Y                             Y�                                                                                                                                                                     ��" �Y��    �}�"    �}    ����   ;�    ;����   ;�Y ��  Y�Y ;��Y;�                           ��  ���Y     ��   �����  �����      ��Y  ������  ���}Y������Y ����  ;����                                         ;����Y                                          Y�" �Y��    �Y;�  ����� ;�Y�}  �Y    Φ Y�Y  �" Y�"   ��  Y����"                          �Y �� �}  ����   �  ;�} � �    Y��Y  ��      Y�}        Y�Y� �� ;�} �                                        Y�  ��Y                                         Y�" �"Y�    �Y�  �}�};� Y�"Φ Y�     �} �Y  �" ��    ;�Y  Φ       ��                   Y�;�Y  ��   ��       ��      ��   ;��Y  ��     Φ        �� ;�Y  ����  ��Y�}  Y�}        ��           ��           ;�Y                                         Y�"        Y�������};�"�}   Y�"Φ�Y      ����     �}    �} Y����"     ��                   �� Y�"  Y�"   ��       �     ;�}  �};�Y  ��     ;�Y        Y�" ��  �� ��  Y�"Y�}  Y�}     ���             ���Y        ��"                                         Y�"          �}�"  릊}   ;�Y�}Y����Y  ��� ;�Y   ;�"     ��;��Y;�     ��                  �} ��"  Y�"   ��       Y�Y   Y��"  Φ ;�Y  ����� Y�����"   Φ   ����  Y�}  ��"           Y���     ��������    ;���     ��Y                                          Y�         �";�   ;����   �����"�� �� �}Y�};�Y   ;�"     ��   �Y   ��������"    Y����     ;�" ��"  Y�"   ��      ;��      � ��  ;�Y     �� ��Y  ��  Y�"  Φ ���  Y�����"          ��"                      ��  ��                                           ;�       ��������    �}��     ���} Y�"�� Y���   ;�"     ��            ��                  ��  Y�"  Y�"   ��     ��        ���������}     ��"Y�"  ;�Y Φ   ��"  ��"     ��           Y���                  ;���    ��                                                        Y� �}      �}Y�"    �"�} Y�"��"  Y��    ;�"     ��            ��                 �}  ;�Y  ��   ��    ��         ��    ;�Y      ��;�Y  ;�Y Y�"   ��"  Y�Y    Φ              ���  ��������  ���Y                                                    Y�"         �}�"   Y� �}��     ��  �� �� Y��  Y��}   �}    �}            ��     ��}      ��";�"   �� �}    ��   �     Y�  ;��     ;�Y ;� � ��  ��Φ    ;�� ��    �� Y�}  ��        ��           ��         ��                                           Y�"        �";�   �����     ;�  ���Y  Y���� Y�    ��    ;�"                   ��       ��"��     ���Y   ������ Y������"�����      ;�Y �����   ���� ��"     ;����   ����   Y�}  Y�Y                                 ��                     4                                  �}                              Y�"   ��                   �Y          �}                                                                                       ��                                                                                            �}                              ��  Y�Y                   Y�          �Y                                                                                      �Y                                                                                                                              �  ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ���� ��    Y���}                     �����"     ��Y   ������    ;����� ������   ������������Y  �����Y�   �}Y����  �����  ��f �    ��Y   Y����Y  �}   ����   �����Y    ����   �����Y   �����/��������� �}   ;��Φ    ;����   ��Y  ���"   ����   ��|������}�}   ;�"     �}    ��"            ;��   ;��    ;���   �  ;�}  ��}  �f �  ;�� �     �      Y��   Y�   �} �}     ��� �� �    ���  ������  �} ;��  ��Y �  ��Y Y�}  Y�} �  ��Y �  �Y   ��   �}   ;�}��   ��"�"  ��}  Y�"�  Y�";�}  ;�}     ��"�}   �}     �}   ���           �Y     �Y   ���  �  � �      �   � �     �     �      �   �} �}     ��� ��  �    ��" ;׻���} �}�}    ��"�  ��}    ��"�  � Y�"        ��   �}   ;�};�Y  Φ; �Y ���  �� Y�Y�  ��"��     ;�} �}    ��     �}  ;�Y�          �� ;����"��  Φ;�Y  �  Y�Y Y�"      �    ���     �     Y�"      �   �} �}     ��릯�"   �    �Φ �����}���}Y�"    ;�}�  �Y�"    ;�}�  �} Y�}        ��   �}   ;�}��  ;�Y Φ ;�o�Φ   ���  �Y�"    ��  �}    Y�    �} �  ;�Y         �Y�} �";�  ;�YΦ  ������  ��      �    Y�"������"������ ��      �������} �}     �����"    �    �}Y�"�"���}�}�}��    ��  ��"��    ��  ��  ����     ��   �}   ;�} ��" ��  �� ���Y�}   ��Y    Y��}     ��"  �}    �Y    �}��    ��Y       �"Y� �"�  �� �� �  �� ��      �    Y�"�     �     �� ����   �} �}     ������   �    �}��� ���} ��"�}��    ������" ��    ������      Y���"   ��   �}   ;�} ;�}Φ   Y�"�}�}Y�"   ��Y    ��    ;�}   �}     ��    �}                 �"Y� �";� ������Y �   �� |�"      �    ���     �     Y�"    ��   �} �}     ���;��   �    �} ��Y ���} ��}Y�"    ;�}�     Y�"    ;�}� ��"       ;��   ��   �}   ;�Y ��;�Y   ;�f�Y �Ɋ�   �ɯ�    ��   ��    �}     Y�   �}                 �Y;�Y Y�"Y� ;�Y  Φ �   �� Q��      �   � �     �     �    ��   �} �}     ��� Y��  �    �} �  ���}  ���}�}    ��"�     �}    ��"� ��      �   ��   �   Y�Y  ����   ��� YỦ   Y�Y�    ��   ��"    �}     �Y   �}                 �� Y������} ��   ��/ �  ;��  ��}  �f �  �� �     �      ��}   ��   �} �}    �� �  ��} �    �}     ���}  ��} Y�}  Y�Y �      Y�}  Y�} �  �� ��   ��Y   ��    ��" ��  ���     ���  ��}  �  ��Y   ��  Y�Y     �}      ��   �}                  ;�Y        Φ    ;�� �����}    ;����� ������   �������       ;������   �}Y�������� �   �� �����/ �}     ���}   ��}   ����   �        ����   �   Y��"�����"    ��     �����     ��Y     Y��  ��" ��  ��  ��  ���������}      ��   �}                   Y�}                                                                                                                                                      ��                                                                                    �}      ;�"  �}                    �����                                                                                                                                                 Y�Y                                                                                   �}      �}  �}          �������                                                                                                                                                            ����                                                                                 ����      YY���}                                                                                                                                                                                                                                                                                                                                      ;��                                                                                                                                                                                                                                                                                 Y�"          �}                 ��         ���       �}            �}     �}                                                                                                                ���Y �} Y��Y               TT                                         �}          �}                 ��         ��         �}     ;�f Y�"�}     �}                                                          �}                                                  ;�Y   �}   ��              d��T                                                     �}                 ��        �}          �}            �}     �}                                                          �}                                                  Y�"   �}   ;�"              ���                                               ����� ����"  ����" "����� ���� ���� ���������" �f���"�} ;�� �} �������Y ����"  ����  ����Y  ��������"����� �����;�Y  Y�*��   ���  �� ;���" ����   �������  Y�"   �}   ;�"              ���                                               � ;�}��" ���  �f �  ���}  �� �}  �  ����" �� �f Y�"�}�  �} ����Y �� ��" �� � �� �� ���  ����"  Y�" �" �}  ;�Y  Y�"Y�" Φ����Y Y�"�� ��Y�" Φ   Φ  Y�   �}   ;�Y    ��Y  �Y  4��l                                                   �f�}  ;�Y��   "Y�  ��Y�  Y�/ �}  Y�  ���}  Y�"�f Y�"��   �} �} Φ  ���}  Y�"��  ;�"�}  ;�YY�  ���}   ��    �}  ;�Y  Y�"�} ;�Y;�"�Ʀ ��; Y���Y �} ;�"   �� �    �}    ��   ��� �Y  T���                                                ;����f�}  �}��     ��   ���������/ �}  ��   ���}  Y�"�f Y�"���   �} �} Φ  ���}  Y�"��   �Y�}  �}��   ���}   ;���"  �}  ;�Y  Y�" �� �� �}Y�Y��}  ���   �� ��   Y�Y ��"     �}     Y�}�Y Y�YY�   ��Td��<                                               Y�� �f�}  �}��     ��   ����      �}  ��   ���}  Y�"�f Y�"���   �} �} Φ  ���}  Y�"��   �Y�}  �}��   ���}     ���� �}  ;�Y  Y�" Y�*�}  ���}�f�Y   ���   Y�/�}  �   �}    �}    ��  �Y  ;��   ���<���                                               ��  �f�}  ;�Y��   "Y�  ����"     �}  ��  ���}  Y�"�f Y�"�};�}  �} �} Φ  ���}  Y�"��  Y�"�}  ;�Y��  ���}       Y�/ �}  ;�Y  Y�" ��"  ���" ���  Y���Y  ��"  ��    ��   �}   �Y              �����                                               �� ��f�} �� �  �� �} ;����  �" �}  ;�} ���}  Y�"�f Y�"�} ��Y �} �} Φ  ���}  Y�"� �� �} �� ;�} ���}   �}  �� Φ  Φ ;��"  ���   ;�� Y��  � ��  ���  Y�Y     Y�"   �}   ;�"              $����l                                               �����f�����  ����" Y����� ����" �}   Y������}  Y�"�f Y�"�}  ��" �} �} Φ  ���}  Y�" ����  �����  Y������}   Y����"  Y�� ;���o�"  ;�}   �  ;�} �� ��  ;�Y  ������  Y�"   �}   ;�"              <���                                                                                               ��             Y�"                                        �}          ��                                                     ��          Y�"   �}   ;�"              L�<                                                                                          �" �Y             ��                                        �}          ��                                                    Φ           �}   �}   ��                  L                                                                                                 ���"             ��Y                                         �}          ��                                                    Y�"            Y��Y �} Y��"                                                                                                                                                                                                                                  �} ��                                 �} ��                                                                                                                                                                                                             ;��                       ��}                                  ��}                                                                                         �� ��                              �� �� Φ�}                                                        �}     �}     ���}                                                                                        Y�} ΂����"  Y�}��                           ;��"��              ;��"                              ;��"               ;����} �������        ���                     �}     �}    Y�;�"  ����   ;�         �����"       ����������Y ������� Y������} �������  ��������� 2�8���}  ��Φ                          �YY�� Y����Q�� ��Y  ���                    �������   �����   ��"     Y�}     �     �       Y�"                      �}     �}           ;�Y�}  �Y         �  �Y      Y��  �}      �     �      ��" �     �  �     � Y�G����� �};�"                                    �} ;��/θY                         �     �       ;�}  ;�}     �      �     �       ��                    �����"������        Y�"Φ Y�          Y�"        ;s�   �}      �     �     ;�}  �     �  �     � �Yuh �Y;�;���                                     �} ;�Y�}�Y ������     ���} Y���"  �     � ������"��"��      Y�Y      �     �      Φ                       �}     �}           Y�"Φ�Y          Y�}       Y�}Y�"   �}      �     �    ��   �     �  �     �                    ;���                               �} ;��"�YY�" ���  Φ ��� Y�" �     �    Φ �Y�"      �������  �     �     ;����                     �}     �}           ;�Y�}Y����Y Y��� ����   Y�Y ��   ������ �     �    ��"   �     �  �     �                   �����                              �} ;�   �Y��    ����  Y�Y  �} �     �    ��  Y��}       ��      �     �      ;�Y                       �}  ������         �����"�� ���}Φ   Y���";�Y  ��   �}      �     �   ;�}    �     �  �     �                   ;��������������������������                  ;���"    ����   ;������} �     �   Y�Y   ��       ������}  �     �      ;�Y                       �}     �}                ���} Y�^�Y ��     ;�� Y�Y Y�"   �}      �     �  ��     �     �  �     �                   �����                                          ���� ����   ;�Y      �     �  �     ��       Y�}      �     �      Y�"                       �}     �}               �"�} Y�^�Y ��     �  Y�}�   �}      �     �  ��"     �     �  �     �                    ;���                                             Y�*��  Y�  Y�}      �     �  ��     ��        ��Y     �     � ;��  ��   ;����Y�� �} ;�Y   �}     �}               ��  �� ���}Φ��   ��Y   ;s Y��  �}      �     � Y�Y      �     �  �     �                                                                 �}  ��"�   Φ ���"  �} �     � Y�Y      ��         �����} ������� ��" Φ    Y�"�� �� �} ;�Y                          ;�  ���Y Y��������"        ����������Y ������� �������� �������  �������                                                                 Y����"       ���} ����}  ������� ������   ��                          �� ���     ���Y                                                                                                                                                                                                                                                                  �"        �Y;�                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ��������                                 ��                                                                                                                                                           �}                                                                                                                 Y�"                                                                                                                       Y�"    ;�     ����        ��"  Y�Y �}  ����� Y�Y��"    �����"    ����Y                           �����"            Y���"            ���"  ���Y  �}            Y�����              �Y   ;���"           Y�    ��      Y�    ��     ;���   ;�Y     ;�}                    Y�"    ;�     ��  �        Φ Φ  �} ��  Y�          Y�}   ;��      �                         Y�}   ;��          ;�Y ��     ��     �"Φ  �Y ��                Y����Y�             Y��Y  �} ��        ���   Y�    ���   Y�    ��}  ��      ;�}                          �����  �}    Y�  �Y Y�"Y�"  �} �}             Y�     �}  ����"   UY UY                Y�     �}         Y�  �"    ��       �}  ���         �}  Y�"�����Y� Y�}          �Y  Y� �Y;s ;s     Y�  �}       Y�  �}        �Y ;�"                             Y�  ��^��  �}     ������  ΫΦ   �} ��Y           �Y ;���� �"� �"  ��Y��"               �Y ����} �"        ;�Y ��     ��       ��      ��        �}  Y�"�����Y� Y�}          �Y  �� �}�;��    Y�  Y�       Y�  Y�      ��}  �}       �}                    Y� �};�    �}     ;�;�"   Y��"   �}  ����"        ;� � � �};�" �" ��"��"                ;�  �� Y�" �}         Y��� ��������" �}   � ��        �}  Y�"Y����Y�              �Y  Y� �Y ��   Y� �} �    Y� �}Y���}    �}Y� ��   ��}                    Y�" ;�Y;�   �����  �� �Y   Φ        �} ��        Y� Y�"     Y��Y ��"Y�";�"  ��������"Y����Y�  �� �� Y�                   ��    �����" ;���"        �}  Y�" Y���Y�             Y���� �} ��  �}�}  Y� Y� �ߦ    Y� Y� Y} ��� ΅�} Y�� ��"                     Y�" ;�Y;�    �}     ;�;�" ;������     ;�Y  ��       Y� Y�"     Y� �����" ��"��"        ;�"     Y�  ����  Y�                   ��                         �}  Y�"   ��Y�                    ;���"  ��     �Y ����      �Y    ��  Y���Y�Y�Q� ��"                      Y�" �};�    ;�Y     ������   Φ    �} �� Φ        ;� � � �}         ��Y��"       ;�"     ;�  ��;��  �}                   ��                         �}  Y�"   ��Y�                          �;��      �� �� ��      ��    Y�"     �};�";� ��                      Y�"  ��Q�� �}     Y�  �Y  Φ    �}  ;����         �Y ;���� �"          UY UY       ;�"     �Y �� ;��"�"               ��������"                     ��  ��"   ��Y�                          ;s ;s      �Y ������    �Y   ��       Y� Y�����"��Y �                  ��"   ����� Y������"          Φ    �}     ���         Y�     �}                       ;�"      Y�     �}                                               �����"   ��Y�                                     ��     ��     ��   Y����}   �Y    ;�  �����                         ;�                            �}      ��          Y�}   ;��                                  Y�}   ;��                                                �}        ��Y�         �}                                                                                                 ;�                            �} �" �}           �����"                                    �����"                                                 �}        ��Y�        �Y                                                                                                                               �} �����                                                                                                               �}                   ���                                                                                                                                      ��     �}    ���    ����Y          ��}                          ;��      ;��   ���         ��   ;�����                 ��YY�     ��        ��     ���     ����"                                  Y�}       ��"   ���                ��"                      �}     ��     ��-đ   ;����   ��Φ  �Y�Y                           �}      ��   ��-đ   ��Φ  �}  ����-đ��Y��"           �};��"     �Y       ��      ��-đ    ����}    Φ�}                          �Y      ��     ��-đ"   Y�Y��"     ��                                                               �Y�Y                                                                                                                                                                                                                           Y��}      ��Y    ��Y    ��Y     ��Y    ��Y    ��}     Y��������}   ;�����������}������}������}������}Y����Y����Y����Y���� ������   ��Y  �}   ����      ����      ����      ����      ����                ����Y�Y�}   ;�}�}   ;�}�}   ;�}�}   ;�}��   �� �      ��Φ     ;���    ;���    ;���     ;���    ;���    ;���     ��Φ       ��}  �f�     �     �     �      �}  �}  �}  �}  ��  ��Y ���  �} ;��  ��Y  ;��  ��Y  ;��  ��Y  ;��  ��Y  ;��  ��Y            ;��  Y�� �}   ;�}�}   ;�}�}   ;�}�}   ;�} ;�}  ;�}�     �}  ��     ���   ���   ���    ���   ���   ���   ;�YΦ      �     �     �     �     �      �}  �}  �}  �}  ��    ����} �}�}    ��"�}    ��"�}    ��"�}    ��"�}    ��" ��   �� �}  ʟ�"�}   ;�}�}   ;�}�}   ;�}�}   ;�}  ��"�� �����Y �} �}    Φ;�Y  Φ;�Y  Φ;�Y   Φ;�Y  Φ;�Y  Φ;�Y   ��Φ      Y�"     �     �     �     �      �}  �}  �}  �}  ��    ;�Y�}���}Y�"    ;�}Y�"    ;�}Y�"    ;�}Y�"    ;�}Y�"    ;�}  �� �� Y�"  ���}�}   ;�}�}   ;�}�}   ;�}�}   ;�}  �Y�" �  ��Y���}     ;�YΦ  ;�YΦ  ;�YΦ   ;�YΦ  ;�YΦ  ;�YΦ  �} ������" ��     ������"������"������"������" �}  �}  �}  �} �����" �}�}�}�}��    릊�    릊�    릊�    릊�    �   ���  �� ;�Φ�}   ;�}�}   ;�}�}   ;�}�}   ;�}   Y��}  �  ��} ��    �� �� �� �� �� ��  �� �� �� �� �� �� Y������      ��     �     �     �     �      �}  �}  �}  �}  ��    �}�} ��"�}��    릊�    릊�    릊�    릊�    �    ��   ���Y Φ�}   ;�}�}   ;�}�}   ;�}�}   ;�}   ��  �  ��}  ;�Y  ������Y������Y������Y ������Y������Y������YΦ  Φ      Y�"     �     �     �     �      �}  �}  �}  �}  ��    ;�Y�} ��}Y�"    ;�}Y�"    ;�}Y�"    ;�}Y�"    ;�}Y�"    ;�}   ���  ��"�}  �}�}   ;�Y�}   ;�Y�}   ;�Y�}   ;�Y    ��  �  ��"�}  �}  ;�Y  Φ;�Y  Φ;�Y  Φ ;�Y  Φ;�Y  Φ;�Y  Φ;�Y  Φ      ;��     �     �     �     �      �}  �}  �}  �}  ��    ���}  ���}�}    ��"�}    ��"�}    ��"�}    ��"�}    ��"  �� �� ;���   Y��   Y�Y�   Y�Y�   Y�Y�   Y�Y    ��  �����" �}  ;�Y  ��   ����   ����   ��"��   ����   ����   ����  Φ       ��}  �f�     �     �     �      �}  �}  �}  �}  ��  ��Y �}  ��} Y�}  Y�Y  Y�}  Y�Y  Y�}  Y�Y  Y�}  Y�Y  Y�}  Y�Y  ��   ��  ��Y  Y�Y  ��" �� ��" �� ��" �� ��" ��    ��  �     �}  ��  Φ    ;���    ;���    ;��Φ    ;���    ;���    ;��}   ������}   ;�����������}������}������}������}Y����Y����Y����Y���� ������  �}   ��}   ����      ����      ����      ����      ����             �a����     �����    �����    �����    �����      ��  �     ����                                                                       ��                                                                                                                                    ��                                                                                                                                           ��                                                                                                                                                                                                                                                                              ���                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ��     ��  Y��                 ���"                     ��"       ��Y  ;��"         ��"  � �                     ��       ��}   ��Y                                     ��Y      ��   ��Y             ;��                                                              ;�Y     Y�"  Ί�}  Y���}         �� ��                      �}      �}   ��Φ         Φ ;����            ��};�  ;�Y      Φ    �ɯ�   ��YY�                            Φ      Y�"    Y��             �� �}                                                            ��    �}   �� Y��"���   ��"�� �� ��                       Y�     Y�  Y�";�Y ;�YY�"  Y�����*� ���*�}  ����Y    ��      Y�   ;�Y�}  �};��"  �}Y�Y                     ;�"     ��    �Y�}  �;�}   ;�" �}      Y�"��                                                                                  ���"                                                                 ��Y                                                                �"                                      �}                                                         ����� ����� ����� �����  ����� ����� ����� ���}  ���}  ����  ���� ���� ���� �}�}�}�} �}Y�Y ����"  ����   ����   ����   ����   ����      ��"   ����� ;�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�*��   �������Y��   ��                                            � ;�}� ;�}� ;�}� ;�} � ;�}� ;�}� ��} Φ�  �f �}  �� �}  ���}  ���}  �� �}�}�}�}     �� ��" �� � �� � �� � �� � �� � ��     ��"  � �� ;�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�"Y�" ΰ�� ��Y�" Φ                                                 Φ    Φ    Φ    Φ     Φ    Φ     ��   Y��    Y�  Y�"Y�  Y�|�  Y�|�  Y�"�}�}�}�} Y�����"�}  Y�"��  ;�"��  ;�"��  ;�"��  ;�"��  ;�"         �� ���";�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�"�} ;�/�}  ;�Y�} ;�"                                              ;����� ;����� ;����� ;�����  ;����� ;����� Y������������     �������"���������������������"�}�}�}�}Y�Y  ;�Y�}  Y�"��   �Y��   �Y��   �Y��   �Y��   �Y���������� Y��Y;�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�" �� ���}  �} �� ��                                              Y�� ΦY�� ΦY�� ΦY�� Φ Y�� ΦY�� Φ��}  ��     ��     ��      ��     ��     ��      �}�}�}�}��   ;�Y�}  Y�"��   �Y��   �Y��   �Y��   �Y��   �Y         ����Y;�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�" Y�/�}�}  �} Y�/�}                                              ��  Φ��  Φ��  Φ��  Φ ��  Φ��  Φ��   ��    ��    ��"     ��"    ��"    ��"     �}�}�}�}��   Y�"�}  Y�"��  Y�"��  Y�"��  Y�"��  Y�"��  Y�"    ��"  ��} Y�";�Y  Y�";�Y  Y�";�Y  Y�";�Y  Y�" ��"�}  ;�Y ��"                                              �� ����� ����� ����� ��� �� ����� ���Y�" Y���  ��  f ��  ���  �"��  �"��  ��}�}�}�}Y�} Φ �}  Y�"� �� � �� � �� � �� � ��     ��"  �� �� Φ ;��"Φ ;��"Φ ;��"Φ ;��"  ��� �} ��   ���                                               ����Φ����Φ����Φ����Φ ����Φ����Φ Y���  ����" ����  ����"  ����" ����" ����" �}�}�}�} Y����  �}  Y�" ����   ����   ����   ����   ����           �����   ;���o�" ;���o�" ;���o�" ;���o�"  ;�Y �����   ;�Y                                                                                                          �}                                                                                                            �}                                        �� �}       ��                                                                                                         �Y                                                                                                                                                     Φ  �}      Φ                                                                                                        ���                                                                                                                                                      Y�"  �}      Y�"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ;�aΦ             �"                        Y�Y  Y�}Y�}    �"                            ��                                                                                                                                                                    ��;�*�}    �"Y�    �"   ����  �Y    ;���Y   ;�" �Y  Y�" ;���|�}                         �Y ���"    �Y  ����Y ����}    ��Y  ;�����Y  Y��� Y������  ;���}  ���"                                        Y����"                                              ��;�"�}    ;� �Y  ;����YY�"�" ��    �} ��  ;�" ��    ��   Y��      �Y                  ;� �� ��  ���Y  ;�  ��";�  Y�Y  ;��Y  ;�Y     Y�"       Φ �} Y�"Φ ��                �Y           ��       Y}  ��                                              ����}  Y�������;�Y�"�}Y��Y�"    �Y ��   ��Y    ;�;���|�}    �Y                  �� �Y ;�"   ;�Y      ;�Y    ;�Y ��Y  ;�Y    �}        Y�" �Y �Y;�Y ;�Y ��"  ��"     ��"             ���         ��                                             ��         �Y;�  ;�"�"  Y�"�"��      ����"      ;�"    �Y  �"      �Y                 �Y ;�Y ;�Y   ;�Y      Y�"    �� �};�Y  ;����Y ����}    Φ   ��Y�� ;�Y �} ��"  ��"   ;��"    �������}    ���      Y�Y                                              ��         ��}  ���"   ����;����}  ��Φ ��    Y�      �}        �������}    ����Y     ;� ;�Y ;�Y   ;�Y     �   ���} �� ;�Y      ��";�Y Y�"   Y�"   Y���} � ;�}           ��                     Y�Y  ��                                               Y�       �������   ���Y     �}Y�;�"Y�릯�    Y�      �}           �Y                 ��  ;�Y ;�Y   ;�Y    �      ;�a������}     ;�Y;�" �}  �}   �Y ;�} Y����Y            ;��"                 ���    �}                                                           �}�     �"��    ;��� �"�� ��Y    ;�    �Y           �Y                �Y  �Y ;�"   ;�Y   �       �}   ;�Y      ;�Y�Y �Y  ��   ;�" Φ    Y�              ��"  �������}  ���                                                         ��        �YY�   Y� �*�}   �Y Y�;�"Y�Y  ���    �Y    ;�           �Y    ��"     �� �"   �� ��    ;�Y  �    Y�  Y�Y   ;�Y  ;�  �� �� �� �}    � ;�Y   Φ  ��"  ;�}       �Y           ��        Φ                                                ��       ��}   �����    ��   ���}  ����}��    ��    ��                  Φ      �� Y�    ���"  �����Y������;����Y    ;�Y  ����  ���Y  ��     ;���}  Y���   ��"  ��                               Φ                                                                     �"                            �Y  ;�"                  ;�Y          �}                                                                                 �}                                                                                                        �"                             Y�}Y�}                   Y�          �"                                                                                 �"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ���}��   Y���"                            Y�����     ��  ;�����Y   ;����Y;�����"  ;�����Q�����  ;����� ;�Y   ;�YY���}���Y;�Y  Y��;�Y    ;��  ��Y;��"  ;�Y  �����  ;�����   �����  ;�����Y   ;����Y�������};�Y   ;�aΦ    �ɯ�  ��   ����  ;���}   Y�YY����� �"  ;�    �"   ;��                     ��   ��   Y��Y  ;�Y  �� Y�}  ;�;�Y  ��Y ;�Y    ;�Y     Y�}  �";�Y   ;�Y ;�Y   ;�Y;�Y ;�Y ;�Y    ;��}  Y��Y;���  ;�Y ��Y � ;�Y �� ��Y � ;�Y  ��" �} �  ;�Y   ;�Y   ;�Y��  �}Y� ;��" �Y� Φ Y�" �}     Φ�"  �Y    �"  �Φ                   �� ����}��  Φ��  ;�Y  Y�"Φ     ;�Y   ��;�Y    ;�Y    Φ      ;�Y   ;�Y ;�Y   ;�Y;�Y;�Y  ;�Y    ;�� ���Y;��Y ;�Y�   Y�";�Y  Y�"�   Y�";�Y  ;�Y ;�"      ;�Y   ;�Y   ;�Y;�Y  Y�;�Y Y��Y ;�" Y�Y��  �� ��     ���"   ��    �" Φ �}                 �YY��}� ;�"Y�" ;�Y  �� ;�Y     ;�Y   ;�Y;�Y    ;�Y    ;�Y      ;�Y   ;�Y ;�Y   ;�Y;�f�Y   ;�Y    ;�f�Y;�Q�Y;�Y��;�Y;�Y   ;�Y;�Y �� ;�Y   ;�Y;�Y  ��  �      ;�Y   ;�Y   ;�YΦ  �� �} �}�� ��   ���Y   ;���Y     Y�" �"   Y�    �"��   ;��                ;��} �}�" �� �} ;�����" ;�Y     ;�Y   ;�Y;�����;�����;�Y      ;�������Y ;�Y   ;�Y;���"   ;�Y    ;�Y�ɯ�;�Y;�Y�};�Y;�Y   �};�����  ;�Y   �};����}    ;���}   ;�Y   ;�Y   ;�Y ��;�Y  ���YY� ��   �     ���     ;�}  �"   �"   �"                         ;��} �}�"�}  �� ;�Y  Y�";�Y     ;�Y   ;�Y;�Y    ;�Y    ;�Y ;���Y;�Y   ;�Y ;�Y   ;�Y;�Y��  ;�Y    ;�Y��";�Y;�Y Y�^�Y;�Y   ;�Y;�Y     ;�Y   ;�Y;�Y�       ��Y  ;�Y   ;�Y   ;�Y �Y��  Y�Q��/�Y   ���Y    ;�Y    ��   �"    �}   �"                         �"Y� �};�;������";�Y  ;�YΦ     ;�Y   ��;�Y    ;�Y    Φ   ;�Y;�Y   ;�Y ;�Y   ;�Y;�Y��  ;�Y    ;�Y �� ;�Y;�Y ���Y�   Y�";�Y     �   Y�";�Y ;�}      �Y  ;�Y   �Y   ;�"  ��Φ   �� ʄ�  Y�"��   ;�Y    ��   �"    Y�   �"                          ��������� ��   �};�Y  ��" Y�}  ;�;�Y  ��Y ;�Y    ;�Y     Y�}  ;�Y;�Y   ;�Y ;�Y   Y�";�Y � ;�Y    ;�Y    ;�Y;�Y  ;��Y ��Y ;�� ;�Y      ��Y ;�� ;�Y  ��Y Y�  Y�"  ;�Y    �� Φ   Y��Y   ��}  ���  �} �   ;�Y   Y�Y    �"    �"  �"                          ��      �}    ��;�����"   ;����Y;�����  ;�����Q�Y      ;����� ;�Y   ;�YY�������} ;�Y  Y��;������;�Y    ;�Y;�Y   ��Y  �����  ;�Y       �����  ;�Y   ��Y����Y   ;�Y     ;���Y    ��     ��Y  Y�} ��  Y�}  ;�Y   ������ �"     �}  �"                            Y����}                                                                                                                                     ;�Y                                                                              �"     Y�  �"                                                                                                                                                                        ���}                                                                            ���}   �|���"         �������                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Y�}          ;�Y              ;�Y        Y���       ;�Y           ;�Y    ;�Y                                                                                                      ��� �"  Y��"             tt                                                ;�Y         ;�Y              ;�Y       �}         ;�Y    ;�Y Y�Y;�Y    ;�Y                                                   ;�Y                                               Y�  �"    ��             ��4                                                            ;�Y              ;�Y       ;�Y         ;�Y           ;�Y    ;�Y                                                   ;�Y                                               ��   �"    Y�            ���                                                     ����} ;����Y  ���� ����Y ���Y����" ����Y;����} ;�YY��Y;�Y ��Y;�Y;���}Y��� ;����}   ���" ;����}  ����Y;��� ����"�����;�Y ;�aΦ  �}�� Φ ΅�� Y���  �}������  ��   �"    Y�            L��                                                         Y�";�� ����   Φ ;�Y�} Y�;�Y  Φ ;�Y;�� Y�";�Y ;�Y;�Y�� ;�Y;�� Y�� Y�";�� Y�" Φ ��;�� ��"Φ ;�Y;��  ;�" �";�Y  ;�Y ;�YY�" Y�"�����YΦ�}Y�" Y�"   Y�"  ��   �"    Y�  ��"  Y�  D��l                                                         ;�Y;�Y ;�Y;�Y   ;�Y ;�Y;�" ;�";�Y  ;�Y ;�Y;�Y ;�Y;�Y ;�Y;��   ;�Y;�Y ;�Y ;�Y;�Y ;�Y ;�Y ;�Y;�Y ;�Y;�Y ;�Y;�Y  ;�Y   ;�Y  ;�Y ;�Y�} �� ;�"Y��YY� ;��� �} ��   �}  ;�"   �"    �} �Y� �}  <�����                                                      Y����Y;�Y ;�Y;�"   ;�" ;�Y;����� ;�Y  ;�" ;�Y;�Y ;�Y;�Y ;�Y;���  ;�Y;�Y ;�Y ;�Y;�Y ;�Y ;�" �Y;�Y �Y;�" ;�Y;�Y   ���� ;�Y  ;�Y ;�Y ���Y �}�Y����   ��"  ���Y  Φ  ��    �"      ��"�  ���   4���$��<                                                     ;�Y ;�Y;�Y ;�";�Y   ;�Y ;�Y;�"    ;�Y  ;�Y ;�Y;�Y ;�Y;�Y ;�Y;�a��  ;�Y;�Y ;�Y ;�Y;�Y ;�Y ;�Y ;�Y;�Y ;�Y;�Y ;�Y;�Y     ;�};�Y  ;�Y ;�Y ;�|�  ���;׻}  ;���  Y�|�  ��   ;�Y   �"    �}            L�����                                                     Y�" Y�Y;�Y �� ��   Φ ��YΦ �";�Y  Φ ��Y;�Y ;�Y;�Y ;�Y;�Y� ;�Y;�Y ;�Y ;�Y;�Y ;�Y Φ ��;�Y ��Φ Y�Y;�Y  Y} �Y�Y �} ��Y ��}   Y�� ��" Φ;�} ��}  ;�Y     ��   �"    Y�            d���                                                      �����Y;����"  ���� ;����Y ���Y ;�Y   ����Y;�Y ;�Y;�Y ;�Y;�Y ;��;�Y;�Y ;�Y ;�Y;�Y ;�Y  ���" ;����"  ;����Y;�Y  ;����  ���} Y����Y  ��"   ;�}  ���� ��  ��"  ������  ��   �"    Y�            ��l                                                                                                ��           Y�"                                    ;�Y        ;�Y                                               �Y           ��   �"    ��             t                                                                                             ����"           ��}                                     ;�Y        ;�Y                                               ��           ��� �"  Y��Y                                                                                                                                                                                                                             Y�"��                               �};�"                                                                                                                                                                                                                              Y��                                ��Y                                                                                                                           ;�"��                                                      �"     �"    ���                                                                                     �Y  ���a��  ��f��                         Y���"            Φ�Y                             ���                 �����;������}       ���                  �"     �"   ����  ����  �Y          ;����        ;���������} ;������}  Y������;������}   ;������}Φ  ��ΦY�" ��;�Y                        �"��}  Y���}��YY�} ��}                    ;������}  ��};�}   Y�Y       ��    ;s    ;s       ��                  ;�����} ;�����}       Y�"�" ��          �} �      ��Y  ��      ;s    ;s      Φ ;s    ;s   ;s    ;s Y� �YY��Y�Y��                                  �" ���ǣ}                         ;s    ;s       Y�" �}        Y�"     ;s    ;s      �}                    �"     �"         Y��Y�"          ;�"       �";�Y   ��      ;s    ;s      �� ;s    ;s   ;s    ;s �";� �"��;��}                                  �" �Y���} ����"�    Y���}���}   ;s    ;s ����� �� ��        ������} ;s    ;s      �Y                    �"     �"         Y�"�"��           �      ����   ��      ;s    ;s     Y�"  ;s    ;s   ;s    ;s                   ���                             �" �Y  �};�" �"��  ;�Y ��} ;�Y  ;s    ;s    Y�" ;���Y         ��     ;s    ;s     Y����"                  �"     �"          ����;����} Y���   ;���} �  ��   ������" ;s    ;s    ;�}   ;s    ;s   ;s    ;s                  ;���f ����� �����������                  ;�Y     ��"��   ��   ��  ;s    ;s   �}   ���          ������ ;s    ;s      Y�                    �"   ;�����}            �}Y�;�/�Y�}     ��Y�  ��   ��      ;s    ;s   ��    ;s    ;s   ;s    ;s                  ;���Y                                       ����   ��"��   �������  ;s    ;s  Φ    ;�Y          Y�Y     ;s    ;s      ��                     �"     �"             ;��� �/�" ��     �Y  ��;�Y   ��      ;s    ;s   ��    ;s    ;s   ;s    ;s                   ���                                          ;�}��  ��   ��       ;s    ;s  ��    ;�Y          ��"    ;s    ;s ;�}  ��   ;�}��Φ Y�"Φ   �"     �"            �Y Y�;�/�Y�} Y�  Y�"   �" ��}  ��      ;s    ;s  Y�Y     ;s    ;s   ;s    ;s                                                             Y} �Y�   ;�Y ��"  Y}  ;s    ;s ;�Y     ;�Y            �����;������} �� �}   ��"�}Φ Y�"Φ                          ��   ���} Y���  ����Y        Y���������} ;������}  �������";������}   ;������}                                                             ;����       Y���};����   ;������} �����  ;�Y                           �� ���    ��;�"                                                                                                                                                                                                                                                       �"       �"��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     �������                                                                                                                                                                                       �"                                                                                                             Y�}                                                                                                                   ��   �}    ;���}        ��  �� �"  ;����" Y�Φ     Y����}   ����"                          Y����}            ����    �Y     ��}  ����   �}           �����}              �"  Y���           ��   Y�     ��   Y�   ����  �}      Y�"                     ��   �}   �} ;s        �} ;� �" �}  �"          ��   ��     ��                         ��   ��         ;�"�}   �Y    �";�"   �Y                Y���}�}             ���" ;�Y ��         ���  �Y    ���  �Y       ;� Y�       Y�"                         ����Y ;�Y   ��   ��  Y��}  �" �}             �� Y���"Y�  Y����    �"�                ������Y�         Y�  ��   �Y       ;�" ��}         ;�"  �� ����}�} ��"          �" Y�  Y� Qs �Y     ��  ��       ��  ��      ;��" �Y                               Y� �Ɋ}�} ;�Y    �����  Φ�  �"  ����Y         �;�Y �Y �Y;�" ��  ���}               ��Y�} �Y        ;�"�}�������}   �}    �}        ;�"  �� ����}�} ��"          �" Y�  Y� Φ;�}    �� �" ��   �� �*����    �"�� Y�"    ;�"                     �� ;�Y�}  ;����}  �� Y�    Y�}       �} ��Y        Y� ��     Y�;� �� ;�}Y�Y  �������}����Y� �Y�Y Y�         ����    �Y     ;�Y     �}        ;�"  �� Y���}�}              �" ;�Y ��   ����   �� �� ���   �� ��� Y�"   ;�/�";��"   Y�}                      ��;�"�}   ;�Y    �� Y�  ;�����}     ;�" �}        Y� ��     Y� ���ߦ �};�Y         �}     �} ���}  ;�                 �Y    ���������         ;�"  ��  Y��}�}             Y���Y Y���   ��Φ   ��;�;�Q�   ��;�   Y�;���}���a�"  Y�Y                       ��;�Y�}   Y�"    �����   �Y   �" ��;�        �;�Y �Y �Y        ���}        �}     ��Y�} �Y                 �Y                        ;�"  ��    �}�}                         Φ;�}      �}�";�     �}   �Y     ;�"�}�"  ��                        ���Ɋ}�}�Y    ��   ��   �Y   �"   ����          �� Y���"Y�           �"�        �}      ���Y Y�|�               �������}                     ;�} Φ    �}�}                         ;s �Y      ;�;�����   ;�  ;�"      �}�����Y ��" ;�                    �� ����Y�������          �Y   �"     ;�Y          ��   ��                                ��   ��                                             ;����¦    �}�}                                   �}    Y�   �}  �����   Y�   �"   �����                          �}                         �" � Y�"           Y����}                                   Y����}                                               ;�"        �}�}        ;�                                                                                              �}                         �" ����Y                                                                                                          ;�"        �}�}      ;��Y                                                                                                                                    Y�}      ��    ��Y    ��}Y�           ;��"                        Y�}      ��Y   ��Y        ��   Y�} Y��                ��"�Y    ��       Y�}      Y��    ��"�Y                                 Y�}       ��    Y��              Y�}                      Y�"     ��    �};�Y  Y���"  Φ;�Y  �";�                        ;�"    Y�"   �};�Y Φ;�"  ��  ;�Y Y�"��;�"��           �}Y��       ��      �Y      ;�"��   �}Y��    ;�"��                          Y�"      ��     ;�"��   ;�"��    �Y                                                             �";�                                                                                                                                                                                                               ���}      ��    ��    ��    ��    ��    ��     ���������  �����Y;�����Q������;�����Q�����o���}Y���}Y���}Y���}�����}  ;��"  ;�Y   �����    �����     �����    �����    �����               ������;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;��}   Y�Y�Y    Φ Y�Y     Y��Y    Y��Y    Y��Y    Y��Y    Y��Y    Y��Y    �YY�     ��"  ;�;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ  ;�� ;���  ;�Y  ��Y �  ��Y �   ��Y �  ��Y �  ��Y �   ��   �"  ��"  ��;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;�YY�" �} �Y    ;�Y �Y    Φ��   Φ��   Φ��   Φ��   Φ��    ����    ��Y�    ;�Y     ;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ   ;�};��Y ;�Y �   Y�"�   Y�" �   Y�"�   Y�"�   Y�"  �} �}  ;�Y  ���;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y �� �� �����;�" ��    ;�"Y�"  ;�"Y�"  ;�"Y�"  ;�"Y�"  ;�"Y�"  ;�YY�  �} Y�    ��     ;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ   Φ;�Y��;�Y ;�Y   ;�Y;�Y   ;�Y ;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y   ��}   �� ;���;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y ;���Y  �Y  ��;�/��     �� �}  �� �}  �� �}  �� �}  �� �}  ���}  Y�" Y�����Y��     ;�����;������ ;�����;����� ;�Y  ;�Y  ;�Y  ;�Y�����  ��;�Y�};�Y ;�Y   �};�Y   �} ;�Y   �};�Y   �};�Y   �}    �}    ���" ��Q�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y  ���   �Y  Y�;�" ;�}   �}  �� �}  �� �}  �� �}  �� �}  �� �}  �� ������    ��     ;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ   Φ;�Y Y�^�Y ;�Y   ;�Y;�Y   ;�Y ;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y   ��}   ���}  ��;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y;�Y   ;�Y  ;�Y   �Y ��;�"  ��   ;������";������";������";������";������";������";�Y  Y�    ;�Y     ;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ   ;�Y;�Y ���Y �   Y�"�   Y�" �   Y�"�   Y�"�   Y�"  �} �}  ;�  ��Y   ;�"�Y   ;�"�Y   ;�"�Y   ;�"  ;�Y   ����� ;�"  ��   ��   �}��   �}��   �}��   �}��   �}��   �}��   Y�     ��"  ;�;�Y    ;�Y     ;�Y    ;�Y     ;�Y  ;�Y  ;�Y  ;�Y Φ  ;�� ;�Y  ;��Y  ��Y ;��  ��Y ;��   ��Y ;��  ��Y ;��  ��Y ;��   ��   �"  ��" �� �� Φ  �� Φ  �� Φ  �� Φ   ;�Y   �Y    ;�" �}   �}    ���}    ���}    ���}    ���}    ���}    ���}   Y������  �����Y;�����Q������;�����Q�����o���}Y���}Y���}Y���}�����}  ;�Y   ��Y   �����    �����     �����    �����    �����             ������    ;���Y    ;���Y    ;���Y    ;���Y    ;�Y   �Y    ;�/���                                                                    �"                                                                                                                              ��                                                                                                                                  ���                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   ���                                                                                                                                                                                                                                ��     ��   ���   ;���Y        ���Y                   ��     ��   Y��        ��" ����            ;���Y  ��Y     ��   Y��  ;���Y                          ��     Y�}   Y��            ��Y ;�"                                                           ��    Φ   Y��� �Y���  Y�"��  ���Y                    Φ     ��   Y�"��  Y�"��  �� ���"��/� ���o�  �Y���    Y�    ��    Y�"��  �Y���  ;�"��                      ��    ;�Y   Y�"��   ;�"��   Y�"  ;�"     ��Φ                                                                                  ���                                                             ��                                                 �        Y�                                     ;�"                                                        ����} ����} ����} ����} ����} ����Y ����Y;���  ;���� ���Y  ���Y  ���Y  ���Y ;�";�";�";�" ;�Y�� ;����}  ���"  ���"   ���"  ���"  ���"     �    ���� ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�aΦ  �};����Φ  �}                                                 Y�"    Y�"    Y�"    Y�"    Y�"    ��    Y�� ��;�}   �} Y��} Y��} Y��} Y�;�";�";�";�"    �} ;�� Y�"Φ ��Φ �� Φ ��Φ ��Φ ��          �Y ��Y ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�YY�" Y�";�� ΦY�" Y�"                                                 ;�Y    ;�Y    ;�Y    ;�Y    ;�Y    ;�"    �Y  ;��   ;�" ;�";�" ;�";�" ;�";�" ;�";�";�";�";�" ������ ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�Y;�Y ;�Y;�Y ;�Y ���������� ;��� ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�Y�} �� ;�"  ��"�} ��                                               Y����Y Y����Y Y����Y Y����Y Y����Y �����" �����������    ;�����Y;�����Y;�����Y;�����Y;�";�";�";�"��"  �� ;�Y ;�Y;�" �Y;�" �Y ;�" �Y;�" �Y;�" �Y          ����� ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�Y ���Y ;�"  �����Y                                              ;�Y ;�Y;�Y ;�Y;�Y ;�Y;�Y ;�Y;�Y ;�Y��" ;�"�� �Y    ��    ;�"    ;�"    ;�"    ;�"    ;�";�";�";�"��   �� ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�Y;�Y ;�Y;�Y ;�Y    �   �ɆY �� ;�Y ;�Y;�Y ;�Y;�Y ;�Y ;�Y ;�Y Y�|� ;�"  �� Y�|�                                              Y�" Y�YY�" Y�YY�" Y�YY�" Y�YY�" Y�Y��  ��"��  ;�� �^�}   Φ �"Φ �"Φ �"Φ �";�";�";�";�"��" �Y ;�Y ;�YΦ ��Φ �� Φ ��Φ ��Φ ��    �   ;�� ;�Y �} ��Y�} ��Y�} ��Y �} ��Y ��}  ;�" �} ��}                                                �����Y �����Y �����Y �����Y �����Y�����"����Y���Y Y���� ���Y  ���Y  ���Y  ���Y ;�";�";�";�" ����}  ;�Y ;�Y ���"  ���"   ���"  ���"  ���"           ����   Y����Y Y����Y Y����Y  Y����Y  ��"  ;�����   ��"                                                                                                       ;�"                                                                                                     �}                                    �Y   ;�"     �Y                                                                                                      ��}                                                                                                                                           ��   ;�"     ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             � �                              �   ��     �                     �                                                                                                                                             � � �   �  �   �    ��   �    ��   �  �  �  � � �                   �  ��   �  ���  ���     � ����  ��  ����  ��   ��                             ���                                                             � � �   �  �  ���� �  � �    �  �  � �    �  ���     �             �  �  � ��     �    �   �� �    �       � �  � �  �                               �                                                            �      ������� �   �  � �    �  �    �    � � � �    �             �  �  �  �     �    �  � � �    �      �  �  � �  �  �  �     ��         ��       �                                                            �       � �   ��    �� � ��   �� �   �    �   �    �����           �  �  �  �    �   ��  �  � ���  ���    �   ��   ���  �  �   ��   ������    ��    �                                                             �     ������   ��     � �  � �  �    �    �          �      ��     �  �  �  �   �      � �����   � �  �  �   �  �    �        �                 �  �                                                                     �  �    � �    � �  � �  ��   �    �          �    �     �  �  �  �  �  �       �    �    � �  �  �   �  �    �  �  �   ��   ������    ��                                                                  �      �  �  ����    �   ��   ��  �  �    �               �     � �    ��  ��� ���� ���     � ���   ��  �     ��   ��   �  �     ��         ��     �                                                                             �                      �  �               �        �                                                       �                                                                                                      �                       ��                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           �� �   ��                               ���     ��   ���     ���  ����   ����� �����   ���  �    � ���  �� �   � �   ��   �� �    �   ���   ����    ���   ����   ���� ����� �    � �    � �  �  � �  � �   � ���� �  �    �   �                          �   �    ��   �  �   �   � �   �  �     �      �   � �    �  �    � �  �  �   ��   �� ��   �  �   �  �   �  �   �  �   � �       �   �    � �    � �  �  � �  �  � �     � �   �   �  � �                        �  �� �  �  �  �  �  �      �    � �     �     �      �    �  �    � � �   �   � � � � � �  � �     � �   � �     � �   � �       �   �    � �    � � � � �  ��   � �    �  �   �   � �   �                       � � � �  �  �  ����  �      �    � ����  ����  �  ��� ������  �    � ��    �   � � � � �  � � �     � �   � �     � ����   ���    �   �    �  �  �  � � � �  ��    �    �   �   �   �                             � � � � ������ �   � �      �    � �     �     �    � �    �  �    � � �   �   �  �  � �   �� �     � ����  �     � � �       �   �   �    �  �  �  � � � �  ��    �    �   �   �   �                             �  ���  �    � �   �  �   � �   �  �     �      �   � �    �  �    � �  �  �   �  �  � �    �  �   �  �      �   �  �  �      �   �   �    �   ��    �   �  �  �   �   �    �   �   �                              �      �    � ����    ���  ����   ����� �       ���� �    � ��� ��  �   � �����     � �    �   ���   �       ���   �   � ����    �    ����    ��    �   �  �  �   �   ���� �    �  �                               ���                                                                                                           �                                                           �    �  �                                                                                                                                              ��                                                         ��     ��       ������                                                                                                                                                                                                                                                                                   �        �           �       ��     �         �    �                                                                          �  �  �                                                                              �       �           �      �       �    �  � �    �                                                                         �   �   �                                                                                 �           �      �       �         �    �                                    �                                    �   �   �           �                                                                ��  ���   ��  ���  ��  ��� ��� ���  � �� �  � � ��� ��  ���   ��  ���   ��� � ���� �� �  � �   � �  �  � � � �   � ���  �   �   �           ��                                                                  � �  � �   �  � �  � �  �  � �  � �  � � �  � �  �  � �  � �  � �  � �  � �� �   �  �  �  � �  �  �  �  �   � �    �  �   �   �   ��  �  � ��                                                                 ��� �  � �   �  � ���� �  �  � �  � �  � ��   � �  �  � �  � �  � �  � �  � �   �  �  �  �  � �  � � � �  �   � �   �  �    �    � �  ��   ����                                                                �  � �  � �   �  � �    �  �  � �  � �  � � �  � �  �  � �  � �  � �  � �  � �    � �  �  �  � �   �� ��   �   � �  �    �   �   �           ���                                                                 ��� ���   ��  ���  ��� �   ��� �  � �  � �  � � �  �  � �  �  ��  ���   ��� �  ���  �  ���   �    �   �  � �   �   ���  �   �   �           �                                                                                              �         �                          �       �                                    �        �   �   �                                                                                                        ��         �                           �       �                                   �          �  �  �                                                                                                                                                                                                                                        �                          �                                                                                           � �                                                          �     �     �                                                                 �  �� �  � �                  �� �        � �                     � �                              ���  �������      ��             �     �    � �  ��   �       ����     �������  ������� ����  �������  ������� �  �� �  � �                  � ����� � �  �              �������  �  �   �                       �     �     �     �             ����� �����      �  � �       �        �   �     �     �    �  �     �  �     �  ��  � �� �                        �  ���                 �     �      � �                        ����  �     �     �               �     �        �  � �       �      � �   �     �     �   �   �     �  �     �                                    �  � � ��� �   �� ��   �     � ���  � �                        �     �     �    ���              �   �����       �� � �� ��   ���  �  �   ����  �     �  �    �     �  �     �             ���                           �    � �  �  �  �     �   �   �                         ����  �     �     �               �     �           � �  �  �     � �  �   �     �     �  �    �     �  �     �             ��� ���� ��������              �   � �  ����  �     �  �    �                         �     �     �  �  �   � �         �     �           � �  �  �     �  � �   �     �     � �     �     �  �     �             ���                             � �  �  �     �     � �     �                          ���  �������  � �    � � � � �                    �   �� ��  ����      �������  ������� ����  �������  �������                                           ���     �� ���  ������� ���   �                                       � ��   � �                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  �                                              �����                      �                                                                                                               �   �    ��       �   � �  ���  � �   ����  ��                   ����        �          ��� ���  �        ���           �  ��        �  �     �   �   ���  �    �                                                     �   �   �   �  � �  � �          �    �  ��                 �    �      � �    �      �  �           �� �          �� �  �      �� �     ��  �     �  �                                                       �  ���  �    ���   � �  � �         �  ��  �� �  � �           � ���  �      �     �     �    �     �  � �� �           � �  � � �   � �      �  �      � �     �                                                 � � �  ���   � �    �     ���       � �    � �� � �   �����    � �  � �          �����  ��� ��      �  � �� �  �       ��� ��   � �  ��  ��   � � ��  �� � ��  �                                                  � � �   �    ���   ���    �  �      � �    �    � �       � �� � ���  �            �                �  �  � �  �                � �   � � �    �    �   � � � �                                                   � � �   �   �   �   �   �  ���      �  ��  �     � �      �    � �  � �                            �  �  � �                  � �    � ����   �   �    � �����                                                   �  ��� ����         �   �    �       �    �                     �    �           �����              ����  � �                        �    �   �   ���  �    �  ���                                                    �                   �    �        ����                       ����                               �     � �      �                                                                                                  �                   � ���                                                                       �     � �     ��                                                                                                                                                                       �     �      � �   � ��   �  �    ��                     �    �    � �   � �   �   �  � � � �         � ��     �       �      � �     � ��    � �                     �     �      � �   �  �    �                                                 �  �                                                                                                                                                                      ��    ��     ��     ��     ��     ��     ��     �����   ���  ����� ����� ����� ����� ��� ��� ��� ��� ����   �    �   ���     ���     ���     ���     ���            ��� � �    � �    � �    � �    � �   � �    �  �   ��     ��     ��     ��     ��     ��    � �     �   � �     �     �     �      �   �   �   �  �   �  ��   �  �   �   �   �   �   �   �   �   �   �          �   �  �    � �    � �    � �    �  � �  ���  �  �  �  �   �  �   �  �   �  �   �  �   �  �   � �    �      �     �     �     �      �   �   �   �  �    � � �  � �     � �     � �     � �     � �     �  �   � �   � � �    � �    � �    � �    �  � �  �  � � �   �  �   �  �   �  �   �  �   �  �   �  �   � ���� �      ����  ����  ����  ����   �   �   �   �  ���  � �  � � �     � �     � �     � �     � �     �   � �  �  �  � �    � �    � �    � �    �   �   �  � �  � ������ ������ ������ ������ ������ ������  ���    �      �     �     �     �      �   �   �   �  �    � �   �� �     � �     � �     � �     � �     �    �   � �   � �    � �    � �    � �    �   �   �  � �  � �    � �    � �    � �    � �    � �    � �  �     �   � �     �     �     �      �   �   �   �  �   �  �    �  �   �   �   �   �   �   �   �   �   �    � �   �   �  �    � �    � �    � �    �   �   ���  �  � �    � �    � �    � �    � �    � �    � �  ����   ���  ����� ����� ����� ����� ��� ��� ��� ��� ����   �    �   ���     ���     ���     ���     ���    �   � � ���    ����   ����   ����   ����    �   �    � �                                                        �                                                                                                                                                                                                                �                                                                                                                                                                                                                                                                                                                                                                                                                �      �   �  �� �        �               �      �   �       �   � �         �� �  �      �   �  �� �                    �      �   �          �  �                                                                �    �   � � � ��  � �  � �               �    �   � �  � �  � � � �� �  �� � ��   �    �   � � � �� �  �                �    �   � �  � �   �   �     � �                                                                                 �                                              ��                                   �       �                           �                                                               ��   ��   ��   ��   ��   ��  ��� ��   ��  ��   ��   ��   ��   � �  �  �    � ���   ��   ��   ��   ��   ��          ���  �  � �  � �  � �  � �   � ���  �   �                                                        �    �    �    �    �    �    �  � �   �  � �  � �  � �  �  � �  �  �  ��� �  � �  � �  � �  � �  � �  �  ����� �  �� �  � �  � �  � �  �  � �  �  �  � �                                                       ���  ���  ���  ���  ���  ���  ������ �   ���� ���� ���� ����  � �  �  � �  � �  � �  � �  � �  � �  � �  �        � � � �  � �  � �  � �  �  � �  �  �  � �                                                      �  � �  � �  � �  � �  � �  � �  �    �   �    �    �    �     � �  �  � �  � �  � �  � �  � �  � �  � �  �    �   ��  � �  � �  � �  � �  �  � �  �  �  � �                                                       ���  ���  ���  ���  ���  ���  �� ���  ��  ���  ���  ���  ���  � �  �  �  ��  �  �  ��   ��   ��   ��   ��          ���   ���  ���  ���  ���   �   ���    �                                                                                               �                                                                          �                           �   �      �                                                                                              �                                                                                                      �    �     �                                                                                                                                                               �;TwOpenGL.cpp m_Drawing==true GL_ARB_texture_rectangle glBindBufferARB glBindProgramARB glGetHandleARB glUseProgramObjectARB glTexImage3D glActiveTextureARB glClientActiveTextureARB glBlendEquation glBlendEquationSeparate glBlendFuncSeparate glBindVertexArray glEnableVertexAttribArray glDisableVertexAttribArray glGetVertexAttribiv m_Drawing==false _TextObj!=__null vector::_M_fill_insert _Font!=__null Cannot unload OpenGL library     m_Drawing==false && _WndWidth>0 && _WndHeight>0 Cannot load OpenGL library dynamically  virtual void CTwGraphOpenGL::DrawTriangles(int, int*, color32*, ITwGraph::Cull)                 virtual void CTwGraphOpenGL::DrawText(void*, int, int, color32, color32)                        virtual void CTwGraphOpenGL::BuildText(void*, const string*, color32*, color32*, int, const CTexFont*, int, int)                virtual void CTwGraphOpenGL::DeleteTextObj(void*)               virtual void CTwGraphOpenGL::DrawRect(int, int, int, int, color32, color32, color32, color32)   virtual void CTwGraphOpenGL::DrawLine(int, int, int, int, color32, color32, bool)               virtual void CTwGraphOpenGL::EndDraw()                          virtual void CTwGraphOpenGL::BeginDraw(int, int)                virtual int CTwGraphOpenGL::Shut() 8ITwGraph    14CTwGraphOpenGL      $F  F          �?      �TwOpenGLCore.cpp Compile failure: %s
 vertex Linker failure: %s
 offset wndSize uv      #version 150 core
in vec3 vertex;in vec4 color;out vec4 fcolor;void main() { gl_Position = vec4(vertex, 1); fcolor = color; }   #version 150 core
precision highp float;in vec4 fcolor;out vec4 outColor;void main() { outColor = fcolor; }     #version 150 core
uniform vec2 offset;uniform vec2 wndSize;in vec2 vertex;in vec4 color;out vec4 fcolor;void main() { gl_Position = vec4(2.0*(vertex.x+offset.x-0.5)/wndSize.x - 1.0, 1.0 - 2.0*(vertex.y+offset.y-0.5)/wndSize.y, 0, 1); fcolor = color; }     #version 150 core
uniform vec2 offset;uniform vec2 wndSize;uniform vec4 color;in vec2 vertex;out vec4 fcolor;void main() { gl_Position = vec4(2.0*(vertex.x+offset.x-0.5)/wndSize.x - 1.0, 1.0 - 2.0*(vertex.y+offset.y-0.5)/wndSize.y, 0, 1); fcolor = color; }        #version 150 core
precision highp float;uniform sampler2D tex;in vec2 fuv;in vec4 fcolor;out vec4 outColor;void main() { outColor.rgb = fcolor.bgr; outColor.a = fcolor.a * texture2D(tex, fuv).r; }    #version 150 core
uniform vec2 offset;uniform vec2 wndSize;in vec2 vertex;in vec2 uv;in vec4 color;out vec2 fuv;out vec4 fcolor;void main() { gl_Position = vec4(2.0*(vertex.x+offset.x-0.5)/wndSize.x - 1.0, 1.0 - 2.0*(vertex.y+offset.y-0.5)/wndSize.y, 0, 1); fuv = uv; fcolor = color; }   #version 150 core
uniform vec2 offset;uniform vec2 wndSize;uniform vec4 color;in vec2 vertex;in vec2 uv;out vec4 fcolor;out vec2 fuv;void main() { gl_Position = vec4(2.0*(vertex.x+offset.x-0.5)/wndSize.x - 1.0, 1.0 - 2.0*(vertex.y+offset.y-0.5)/wndSize.y, 0, 1); fuv = uv; fcolor = color; }      virtual void CTwGraphOpenGLCore::DrawTriangles(int, int*, color32*, ITwGraph::Cull)             virtual void CTwGraphOpenGLCore::DrawText(void*, int, int, color32, color32)                    virtual void CTwGraphOpenGLCore::BuildText(void*, const string*, color32*, color32*, int, const CTexFont*, int, int)            virtual void CTwGraphOpenGLCore::DeleteTextObj(void*)           virtual void CTwGraphOpenGLCore::DrawRect(int, int, int, int, color32, color32, color32, color32)                               virtual void CTwGraphOpenGLCore::DrawLine(int, int, int, int, color32, color32, bool)           virtual void CTwGraphOpenGLCore::EndDraw()                      virtual void CTwGraphOpenGLCore::BeginDraw(int, int)            virtual int CTwGraphOpenGLCore::Shut()          18CTwGraphOpenGLCore     /G                    0���@���@���@���@��� ���X��� �������؊���������x�������ؙ������������������h�������h������(���H���h�������ؘ��������������0���`���@���@���@�����������Л��������`���(������h��� ���ء������(�������X�������8������P���ȟ��@��� ���ȱ�� �������X���(���@��� �������0���������������������������������`������� �����������Ю����������(��������!��!��!��<!�����������l ��� ��|��|%���%���%��d&��'��t(��t'���'���(���(��$)���)��*���*��l+��l+��l+���,��T0���/��,0���/��T0��T0��0��T0��T0��\/��1��D4��D4��D4��D4��l1��D4��D4���1���1��42���2���3���3���3���0��47���:��:��:��:��T:���7���:���7���7��8���6���;���;���;���;���;���;���;���;��/<���;���;��$<���;���<��:<��`<���B��dB��DB���A���A���B���B���B���B���B���A��,A���@���@���@��\@��$@��@���?���?��D?��4?��$?���>���?��T?���>���>��D>���c���c��Id��Id��Id���d���e���e���d��Je���b��:c���f���f���f���f���f��{f���e���f���f���e���e���e���e���e���e���e��bool CTwBar::EditInPlaceGetClipboard(std::string*)              bool CTwBar::EditInPlaceEraseSelect()                           CTwBar::CEditInPlace::~CEditInPlace()                           CTwBar::CEditInPlace::CEditInPlace()                            double CTwBar::RotoGetStep() const                              double CTwBar::RotoGetMax() const                               double CTwBar::RotoGetMin() const                               void CTwBar::RotoSetValue(double)                               double CTwBar::RotoGetValue() const                             int CTwBar::LineInHier(CTwVarGroup*, CTwVar*)                   bool CTwBar::OpenHier(CTwVarGroup*, CTwVar*)    bool CTwBar::KeyTest(int, int)                  bool CTwBar::KeyPressed(int, int)                               bool CTwBar::MouseWheel(int, int, int, int)                     bool CTwBar::MouseButton(ETwMouseButtonID, bool, int, int)      bool CTwBar::MouseMotion(int, int)              void CTwBar::Draw(int)          void CTwBar::DrawHierHandle()   void CTwBar::Update()                           void CTwBar::BrowseHierarchy(int*, int, const CTwVar*, int, int)                CTwBar::CTwBar(const char*)                     virtual void CTwVarAtom::Increment(int)                         virtual void CTwVarAtom::ValueToString(std::string*) const      ����   �label help visible readonly show readwrite keyincr key keydecr min max step precision hexa decimal enum open close opened typeid valptr noalpha coloralpha hls colormode colororder arrow arrowcolor axisx axisy axisz showval basic_string::at TwBar.cpp _Step==1 || _Step==-1 text position refresh fontsize fontstyle valueswidth iconpos iconalign iconmargin movable iconifiable fontresizable alwaystop alwaysbottom iconified colorscheme contained buttonalign iconify %d%d%d%d dark light %f fit center m_Font _Root!=__null m_Roto.m_Var!=__null basic_string::substr m_EditInPlace.m_Active basic_string::erase _OutString!=__null _Str!=__null SHORTCUTS (read only) (none)    {struct} unreachable %c (0x%.2X) %c (%d)   (0) 0x%.2X %u 0x%.4X 0x%.8X %g %%.%df %%.%dlf unknown type dx ogl %p rgba argb %lf %lf %lf off %d %d %d +x -x +y -y +z -z {} <> []   - %hd %hu %u %c%n  ,%n m_UpToDate==false Keys:  Key:  Fit column content sProxy!=__null atb   %d.%02d ~ Enum Popup ~ Must be of type Enum Unknown type Bad value Value required Value is not a group Invalid parameter Unknown parameter    CTwVarAtom::Increment : unknown or unimplemented type
  g_TwMgr!=__null && g_TwMgr->m_Graph!=__null     Labels.size()==Colors.size() && Labels.size()==BgColors.size()  BgColors.size()==Values.size() && Colors.size()==Values.size()  it->second.m_Var==mProxy->m_VarParent   g_TwMgr->m_Graph && g_TwMgr->m_WndHeight>0 && g_TwMgr->m_WndWidth>0 6CTwVar 10CTwVarAtom 11CTwVarGroup  �������������            ��.A  �����A      ��  �����A
.>=���|�=0�p~��=�Z��q=�������?    ���>  �>���=��L=��>fff?��L?���>��L>33�>  @?�1�@�I?��@�I@  ��            ���               �            �������               �        Ignoring Xlib error: error code %d request code %d
     Q={x:%.2f,y:%.2f,z:%.2f,s:%.2f} mProxy->m_VarParent->m_Vars.size()==8   mProxy->m_VarParent->m_Vars.size()==16  s_SphTri.size()==3*s_SphCol.size()      s_SphTriProj.size()==2*s_SphCol.size()  s_SphColLight.size()==s_SphCol.size()   s_ArrowTriProj[j].size()==2*(s_ArrowTri[j].size()/3) && s_ArrowColLight[j].size()==s_ArrowTri[j].size()/3 && s_ArrowNorm[j].size()==s_ArrowTri[j].size()        g_TwMgr->m_ClientStdStringStructSize==sizeof(std::string)       m_Graph!=__null && _Bar!=__null m_Bars.size()==m_MinOccupied.size()     BarOrderIt!=g_TwMgr->m_Order.end()      itm!=g_TwMgr->m_MinOccupied.end()       i>=0 && i<(int)g_TwMgr->m_Bars.size()   _Bar!=__null && _HasValue!=__null && _Attrib!=__null && strlen(_Attrib)>0       Unable to set param '%s%s%s %s' %s%s    _Code!=__null && _Modif!=__null g_TwMgr->m_Bars.size()==g_TwMgr->m_Order.size() Parsing error in def string%s [%-16s...]        Parsing error in def string: Bar not found%s [%-16s...] Parsing error in def string: Variable not found%s [%-16s...]    Parsing error in def string: Unknown attribute%s [%-16s...]     Parsing error in def string: '=' not found while reading attribute value%s [%-16s...]   Parsing error in def string: can't read attribute value%s [%-16s...]    Parsing error in def string: wrong attribute value%s [%-16s...] _GetCallback==__null && _SetCallback==__null && _ButtonCallback==__null _GetCallback!=__null || _Type==TW_TYPE_BUTTON   _GetCallback!=__null || _SetCallback!=__null    s.m_Size>0 && s.m_ClientStructSize>0    !(s.m_IsExt && (m.m_Type==TW_TYPE_STDSTRING || m.m_Type==TW_TYPE_CDSTDSTRING))  g_TwMgr!=__null && _Grp!=__null _Font!=__null && _String!=__null        _Grp!=__null && g_TwMgr!=__null && g_TwMgr->m_HelpBar!=__null   The RotoSlider allows rapid editing of numerical values.        To modify a numerical value, click on its label or on its roto [.] button, then move the mouse outside of the grey circle while keeping the mouse button pressed, and turn around the circle to increase or decrease the numerical value.       The two grey lines depict the min and max bounds.       Moving the mouse far form the circle allows precise increase or decrease, while moving near the circle allows fast increase or decrease.        basic_string::_S_construct null not valid       Unable to get param '%s%s%s %s' %s%s    enumIndex>=0 && enumIndex<g_TwMgr->m_Enums.size()       g_TwMgr->m_Structs.size()==structIndex+1        A 4-floats-encoded RGBA color.  true='HLS' false='RGB' readwrite        true='Axis Angle' false='Quaternion' readwrite hide     A 4-doubles-encoded quaternion  g_TwMgr!=__null && g_TwMgr->m_Graph==__null     Value count for TW_PARAM_CSTRING must be 1      Debug/Release std::string mismatch      Name cannot include back-quote  Asynchronous processing detected V={%.2f,%.2f,%.2f} A=%.0f%c V={%.2f,%.2f,%.2f} black white  line %d TwMgr.cpp g_TwMgr->m_Graph->IsDrawing() ext!=__null varGrp->m_Vars.size()==16 s_SphTri.size()>0 overlap m_Graph!=__null ERROR(AntTweakBar) >> %s
 %s(%d):  fixed default bl lb bottomleft leftbottom br rb bottomright rightbottom tl topleft lefttop topright righttop vert vertical horiz horizontal _Var==__null _Bar!=__null && _AttribID>0 GLOBAL  :  / Unknown var '%s/%s' Unknown param '%s%s%s %s' SHIFT CTRL META ALTGR ALT backspace bs tab clear clr return ret pause escape esc delete del ins home end pgup pgdown _String!=__null SHIFT+ CTRL+ ALT+ META+ BackSpace Tab Clear Return Pause Esc Delete Right Left Insert Home End PgUp PgDown F1 F2 F3 F4 F5 F6 F7 F8 F9 F10 F11 F12 F13 F14 F15 Unknown Key pressed: %s undefined vector<bool>::_M_insert_aux %s%s fontscaling State==PARSE_ATTRIB Bar!=__null %s%s [%-16s...] readonly  TW_UNNAMED_%04X `/` `  s.m_Size>0 label=` ` group=` typeid=%d valptr=%p close  _Grp!=__null _ToAppend!=__null Structures Structure RotoSlider  

ף=
�#>�W?  pA    ���  �?   ���=��cܥL@   ���z>H�����z>9�R�Fߑ?�+����=   ����?`P���:      �?       @      @          �?                  ��  ��                  �?          �?  �?  ��  ��              �?          �?          �?          �?          ��          ��          ��          ��    ����@���@�@���@��@��@@��@@@��@@���������      glDeleteVertexArrays glGenVertexArrays glIsVertexArray  AntTweakBar: OpenGL Core Profile functions cannot be loaded.
   �������������������������������������������������������������������������������E���m�������w���������������������c���;���Y���O���)������������������������������������������������ݛ��˛��������������;���                  Unsupported SDL version ��������ĝ���������������������������������������������������������`���`���x�������`��� �������О��О��`���`���`���`���`���`���`���`���`���`���`���`�������l���������������������������������������������������D������̠��������ԟ��H���á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��á��h���a�������}���v���o�������������������á��á��á��á������������������̡��̡��,���̡�����������̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̡��̢��������w���w���w������w���w���w���w���w���w���w���w���w���w���w���w���w������w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w��� ���������Т������w���w�������w���w���w���w���w���w���w���w���w���w���w���*���w���w���w���w���w���w��� ���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w������w���w���w���w���w���w���w���w���w���w���w��� ���������Т������>���4�������w���w�������w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w��� ���%���������ڣ��У��ƣ��/���H���R���\���f���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���p���p�����������w���w���w���w�����������w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w���w�������P   ;8  �  :��T  O��  "O���3  8O���4  \O���1  lO���_  |Q��`  �Q���`  �R��|  �T���  �U���  �X���  �Y��
���]  ���^  ,
 AABG        �   �H��          4   �   �K��	   N�N�D �GP�
 AABC        �   �L��m             �    N���               �Q��2    A�p              zPLR x�%�  �  T   $   �Q��n  �U  B�B�B �B(�A0�A8�J��
8A0A(B BBBA          �  �b��u   K �
A     $   �  d���    A�A�D �AA   �  ؏��                Џ��    D Y    $  ؏��    D0X    <  Pd��              T  Hd��8    A�G nA $   t  hd��r    A�A�G0fAA   �  ����              �  x���              �  ����           <   �  xd��   B�B�D �A(�D@\
(A ABBA    L   $  Hh��Y   B�E�B �B(�A0�A8�DP�
8F0A(B BBBK     L   t  Xi���   B�B�B �B(�A0�A8�DP�
8A0A(B BBBD    d   �  �j��   B�B�B �B(�A0�A8�D�_
8A0A(B BBBIR
8A0A(B BBBD   L   ,  �l���   B�B�B �B(�A0�A8�D�~
8A0A(B BBBA      |   t���    D�4   �  xt���    O�H�A �H@� A�A�B�        �   u��Z    aw 4   �  Hu���   A�D�D0C
FAEeAA        �v��z    D 
E      d   <  �v��   B�B�B �B(�A0�D8�D@A
8A0A(B BBBDb
8F0A(B BBBG      �  �y��v    D�N
E   <   �  �����   B�B�A �A(�G@k
(A ABBB     D     H����   B�B�B �A(�D0�G@H
0A(A BBBF     D   L  �����   K�B�A �A(�GP1(A� A�B�B�CP����L   �  8y���   B�E�B �B(�A0�A8�G��

8A0A(B BBBC      �  ����    D Y    �  ����    D0X      ����              ,  ����8    A�G nA    L  ����"              d  Ȓ��)              |  `���              �  X���           <   �  ����   B�B�B �A(�A0��
(A BNBE     �  �����   A��
G   L     ���]   B�E�B �B(�A0�A8�D�
8A0A(B BBBG   L   \   ���]   B�B�B �B(�A0�A8�G�	
8A0A(B BBBD   4   �  ���   B�A�A �G0�
 CABD       �  �����    D�   �  p���Z    aw    	  ����v    D�N
E   ,   4	  ����    B�H�A ��AB      L   d	  �����   B�I�B �B(�A0�A8�O��8F0A(B BBB         �	  H���M    A�c
L\ d   �	  x����   B�B�E �B(�A0�A8�Dp
8A0A(B BBBIM
8A0A(B BBBI    d   <
  Ы��,   B�E�B �B(�A0�A8�DPI
8A0A(B BBBD 
8A0A(B BBBN   <   �
  h����   B�B�A �A(�G@k
(A ABBB     D   �
  ����   K�B�A �A(�GP1(A� A�B�B�CP����L   ,  ���S   B�B�E �B(�A0�A8�G�?

8A0A(B BBBC      |  �.��              �  �.��              �  �.��%              �  �.��           ,   �  �.��[    B�D�E �LAB      4     �.��l    A�D�F O
AADDFA       D  x���m          \   \  ����Q   B�H�B �B(�D0�C8�DP�
8A0A(B BBBBd8C0A(B BBB   �  �.��           4   �  ����G    B�D�D �q
ABBAAD      
CAI      L   T
(C ABBFA(J ABB     4   �
ADB$AB    �
(A BBDE^
(A BBBC L   D  ����$   B�B�E �B(�A0�A8�DP�
8D0A(B BBBA     ,   �  -���    A�A�G0@
AAA     ,   �  �-���    A�A�G0H
AAA     ,   �  ����   A�A�G0�
AAD        $  ����    A�L       ,     ����i  �G  A�D�D0�
AAA    t  ����    A�L          �  �-��u              �  `.��F    A�|          �  ����B              �  x.��              �  ����                ����B           D   ,  ���   A�A�G0O
AAFd
AAJ�
FAC    L   t  ����   A�A�G@J
AAK_
AAG}
AAIS
AAK $   �  ����5   A��
D]
C    t   �  ����C	   B�B�A �C(�D@�
(A ABBF�
(A ABBB|
(A ABBF�
(A ABBB        d  ����T              |  �����    A��      L   �  @���\   A�A�F @
AAFa
FAH�
FAEDFA      �  P���	                H���           ,     @���   A�D�Q m
CAI         L  0���           L   d  (���Z   B�B�A �A(�DpB
(C ABBD�
(C ABBI  <   �  8����    B�B�A �A(�D@N
(A ABBB     D   �  ����:   B�B�B �A(�A0�J��
0A(A BBBD   L   <  ���2   B�B�B �B(�A0�A8�G`8D0A(B BBB       L   \  ���t  �C  B�B�B �B(�D0�D8�Dp�
8D0A(B BBBA L   �  ���a   B�E�B �B(�A0�A8�D��
8A0A(B BBBK   L   ,  ����    B�H�B �A(�A0�s
(A BBGCa
(A BBDA d   |  ���   B�E�B �B(�A0�A8�DP�
8A0A(B BBBKD
8D0A(B BBBA     d   �  8���   B�B�B �B(�A0�A8�D�I
8A0A(B BBBG
8A0A(B BBBG     L  `��              d  h��5    ]    $   |  ���V    A�\
CR
E         �  ���W    D s
A         �  ��W    D s
A         �  H��W    D s
A      d     ���7   B�B�B �B(�A0�A8�G�k
8A0A(B BBBJ{
8A0A(B BBFO      l  `%��g    A�G0YAL   �  �%���   B�B�B �B(�A0�A8�G�g
8A0A(B BBBN     $   �  `+��,   A�GP"A            h-��                p-��              4  x-��           4     �-���   �?  B�A�A �G0r
 AABA 4   T  (.��'  �?  B�A�A �D0g
 AABA    �   /���           T   �  �/���  �?  B�B�B �B(�A0�A8�J�S
8A0A(B BBBG          ,  @6��C           ,   D  x6��    A�A�Gp{
AAJ     4   D  H9��_  ,?  B�A�D �GP�
 AABF <   �  p:���    B�A�A ��
ABEC
CBA       D   �  ;��   e�B�A �A(��� A�B�B�J(����       4   4  �;���    B�A�A �G0�
 FABA     4   l  �<���    B�C�A �N
ABI�AG  \   t  =��{  >  B�B�B �A(�A0�G�e
0C(A BBBBg
0C(A BBBG L     h$��L   B�B�B �B(�A0�A8�GP_
8A0A(B BBBF      T   $  �B��1
(A ABBB�
(A ABBE   D   �  %���   B�B�B �A(�D0�G@L
0A(A BBBJ     L   �  xO��#  6=  B�E�B �B(�A0�A8�F`k
8C0A(B BBBF \   D  XU��
   B�E�E �D(�C0�Dp}
0C(A BBBB
0E(A BBBH       D   �  �%���   B�B�B �A(�D0�G@\
0A(A BBBJ     �   �  �^���   B�E�E �F(�A0�G@z
0C(A BBBBY
0C(A BBBE^
0C(A BBBHA
0C(A BBBE_
0C(A BBBG|   �  �_���   B�E�E �E(�F0�A8�G`x
8A0A(B BBBGz
8F0A(B BBBG�
8A0A(B BBBE   T   �  e��R  <;  B�E�E �E(�F0�A8�J�P
8A0A(B BBBD       L   \  m���   B�H�F �D(�GPq
(A ABBF}
(F ABBH   d   |  `%��A  �:  B�B�E �B(�E0�D8�D@L
8F0A(B BBBA�
8D0A(B BBGAL     H&���   B�B�B �B(�A0�A8�Gp�8A0A(B BBB       L   4  �q���  #:  B�E�B �B(�E0�A8�Gp
8A0A(B BBBF4   �  Hx��z  �9  B�A�A �G0
 AABFL   �  @'���   B�B�B �B(�A0�D8�GPc
8A0A(B BBBG     L     �(��  �9  B�B�B �E(�A0�D8�G��
8A0A(B BBBAT   \  �y���  �9  B�B�B �E(�A0�A8�G��
8A0A(B BBBA       T   �  h���\  S9  B�B�B �B(�A0�A8�G��
8A0A(B BBBC       d   <   �+��E   B�B�B �E(�D0�E8�D@P
8F0A(B BBBA�
8D0A(B BBGA    L   �   �,���   B�B�B �B(�A0�A8�D� 
8A0A(B BBBA   L   �  �����  �8  B�E�B �B(�D0�D8�I�S
8A0A(B BBBA�   D!  1��   N�E�E �B(�A0�A8�DP�8A�0A�(B� B�B�B�HP������
8A�0A�(B� B�B�B�O�
8A�0A�(B� B�B�B�H    L   �!  �����   B�B�E �B(�A0�A8�D`�
8A0A(B BBBK     T   !  (3���  s7  B�B�B �B(�A0�D8�D��
8A0A(B BBBB       d   �"  �9���   B�B�B �B(�A0�D8�DPY
8A0A(B BBBD�
8A0A(B BBBF  T   �!  X���=  37  B�B�B �B(�A0�A8�J�
8A0A(B BBBD       T   "  @����/  37  B�B�E �B(�A0�A8�G�s	
8A0A(B BBBG       L   �#  ����+   B�B�B �E(�A0�A8�Dp
8A0A(B BBBA    L   �#  ����~   B�B�B �B(�A0�A8�I��
8A0A(B BBBA   $   D$  ���?   A�D �
AC     4   l$  ���)   B�A�A �D@�
 AABH     L   �$  �
(A BBBBA
(A HBBB \   �$  ���i   B�B�B �A(�A0�DP@
0A(A BBBDF
0A(A BBBJ        T%  ���              l%  P=��              �%  ����           $   �%  @=��2    A�A�D iAA $   �%  X=��2    A�A�D iAA    �%  `���           $   &  X=��2    A�A�D iAA    ,&  @���           $   D&  X=��2    A�A�D iAA L   l&  p=���   B�B�B �E(�A0�A8�DP
8A0A(B BBBG       �&   ?��+    Df ,   �&  8?��b    A�A�D w
AAA      ,   '  x?��b    A�A�D k
AAM         4'  �?���              L'  ���    AS L   d'  (@���   B�B�B �E(�D0�A8�G�|
8A0A(B BBBA       �'  �A��#          L   �'  �B��r   B�E�B �A(�A0�/
(A MBBLA
(A BBBH4   (  �C��k    A�A�GP{
DAGUDA        T(  ���     H    $   l(  D���    A�I�G vGA   �(  �D��w              �(  �D��    A�Z          �(  �D��    A�Z       L   �(  �D��z   B�B�B �B(�A0�D8�D@d
8A0A(B BBBA      $   <)  P���v    A�G z
AA      $   d)  ����~    A�G B
AA     l   \(  �E��'  1  B�E�B �E(�A0�A8�D�M
8A0A(B BBBE�
8A0A(B BBBA      ,   �)  �J��   A�GP�
AH{A       ,   ,*  �K��P   B�G�A �
ABK  $   \*  �N��X    A�D�G IAA,   �*  �N��
AGkA       ,   �*  �O���   A�D�D0T
AAA       �*  XS���   DPi
C        +  (U��U   D�P        $+  hV��U   D�P        D+  �W��R   D�M        d+  �X��R   D�M     ,   �+  (Z���    A�G@m
AJwA      T   �+  �Z��
AABm
AAAs
AAK�
AAD         ,  �^��          T   $,  �_��   A�A�GP$
AAA�
AAAr
AADn
AAH       $   |,  `d���
   n�D��H�L   �,  �n��,   F�Y�H �H(�G`�
(A ABBD�
(A ABBA  D   �,  �p��d   A�A�G0_
DAK�
AALD
AAJ       <-  P����           L   T-  �t���   B�B�B �B(�A0�A8�G��
8A0A(B BBBA      �-  h���'           ,   �-  ����1    B�E�D �bAB          �-  ����x    Dv
A      <   �,  ����  �,  G�A�A �G`I A�A�B�C`���    L.  P���              d.  X���?    g       |.  ����x    Dv
A      <   l-  �����   a,  A�D�D y
AADG
AAG        �.  ����O           <   �.  �����   B�E�D �C(�N0�(A ABB       L   4/  8����    B�B�D �A(�D0[
(A ABBBD(F ABB      ,   �/  h���G   A�D�Q m
CAI      $   �/  ����   D�
KD
A       ,   �/  �����    A�A�G �
AAA     ,   0  P���u    A�D�D s
AAB      ,   <0  �����    A�D�D {
AAJ      <   l0   ����   B�B�A �D(�D0n
(A ABBA       �0  ����R    Da
K         �0  ����2    \U ,   �0  ����    A�A�D u
AAC         1  h���+    UO 4   ,1  �����    B�A�A �DP�
 AABC     D   d1  H���k   A�A�D@�
AAH�
FADk
CAI   l   �1  p����   B�B�B �D(�A0�F`Y
0F(A BBBIX
0A(A BBBHl
0C(A BBBA    2  ����              42  ����              L2  ����              d2  ����3    SS    |2  ����              �2  ����              �2  ����-    Y�S�      d   �2  �����   B�B�B �B(�A0�D8�D@|
8A0A(B BBBA�
8F0A(B BBBD   t   2  X���2  �'  B�B�B �A(�A0�D@�
0A(A BBBD�
0A(A BBBFs
0C(A BBBA        �3   ���v    GSD   �3  �����   B�A�H ��
ABBT
ABE�
AGA  D   4  0���5   B�A�H ��
ABBT
ABE�
AGG  L   T4  (���	   B�B�B �B(�A0�A8�D`�
8D0A(B BBBD       �4  ���M    wU �   �4   ���   B�B�B �D(�A0�D`�
0F(A BBBGZ
0A(A BBBF{
0C(A BBBCL
0F(A BBBGL
0F(A BBBG$   T5  �����    DS
IG
A          |5  ���:    dU    �5  8���g    D~
FK
A   �5  ����P    A�{
AR 4   �5  �����    A�A�D a
AAGI
FAH    6  P����              $6  ȼ���    A�j
E   ,   D6  X���[    B�D�D �MAB         t6  ����(              �6  ����(           T   �6  ����    B�A�A �G@�
 CABHU
 AABG�
 CABC      d   �6  `���O   B�E�E �E(�D0�F8�G`
8A0A(B BBBEF
8A0A(B BBEA   $   d7  H����    Dy
CV
JG
E  $   �7  �����    Dx
DS
MD
E  T   �6  X����  p#  B�B�B �B(�A0�A8�J�
8A0A(B BBBF       L   8  ����   B�E�B �B(�A0�A8�Dp�
8A0A(B BBBA     ,   \8  ����/   A�D�D0C
AAA     D   �8  0���Z   B�B�B �D(�A0�D@Z
0C(A BBBE    ,   �8  H���E   B�D�D ��
ABD   L   �7  h���	  n"  B�E�B �B(�A0�A8�G��
8A0A(B BBBE   T9  8���              l9  0���              �9  8���!              �9  P���              �9  H���
              �9  @���%           <   �9  X���~   B�A�A �a
ABH�
ABL      <   $:  ����   B�B�A �D(�D0�
(A ABBE    L   d:  h����    B�A�A �G0a
 AABHR
 AABEl AAB     �:  ����"              �:   ���"           L   �:  h���I   B�E�B �B(�A0�A8�Dp�
8A0A(B BBBA     L   4;  h����   B�B�B �B(�A0�A8�D�)
8A0A(B BBBA   4   T:  ����s      B�D�D �G0~
 DABA  ,   �:  0���M   �  A�D�G0_
DAA  ,   �:  P���B   �  A�D�G0T
DAA  D   <  p����    B�B�E �A(�A0�D@P
0A(A BBBA     D   d<  �����   B�B�B �A(�D0�G@L
0A(A BBBJ     D   �<  @����   B�B�B �A(�D0�G@\
0A(A BBBJ     L   �<  ����	   B�E�E �B(�D0�A8�G��
8A0A(B BBBG   D   D=  X���E   B�B�B �A(�A0�GP�
0A(A BBBH     d   �=  ����   B�D�B �B(�A0�D8�GPn
8A0A(B BBBB�
8F0A(B BBBA    d   �<  �����  �  B�B�B �E(�A0�D8�Dp
8A0A(B BBBAi
8F0A(B BBBA�   ,=  ����  e  B�B�B �B(�A0�A8�D�C
8A0A(B BBBA
8C0A(B BBBAV
8C0A(B BBBA     L   �>  ���M   B�B�B �B(�A0�D8�Dp18A0A(B BBB          4?  ���D    tO <   L?  ����   B�H�F �D(�G@v
(A ABBI     $   �?  `����    Ds
QY
OJ
E  D   �?  �����   B�B�B �A(�D0�G@L
0A(A BBBJ     D   �?  0����   B�B�B �A(�D0�G@H
0A(A BBBF     d   D@  x����   B�B�B �E(�D0�D8�Gp�
8A0A(B BBBI`
8A0A(B BBBA     d   |?   ���H  6  B�B�A �A(�D�Y
(A ABBG�
(C ABBJV
(C ABBB     A  H���D    tO T   �?  �����    B�B�B �B(�A0�D8�I�*
8C0A(B BBBD       T   T@  (����  �  B�B�B �B(�D0�A8�G�A
8C0A(B BBBA       L   �@  �
��/  �  B�B�B �B(�A0�A8�G��
8A0A(B BBBG   ,B  �#��3    D0n    DB  �#��6    D0q    \B   $��2    D0m    tB  ($��2    D0m    �B  P$��@    K0t T   tA  x$���  A  B�B�B �E(�A0�A8�D�U
8A0A(B BBBA       L   �B  ����   B�B�E �F(�A0�A8�DPh
8A0A(B BBBA     L   B  ����;  �  B�E�E �E(�A0�A8�FP
8A0A(B BBBFd   �C  p���   B�E�B �E(�E0�A8�D`2
8D0A(B BBGDt
8F0A(B BBBE    L   D  �*���   B�B�B �B(�A0�A8�D@�
8A0A(B BBBA    L   TD  �����   B�B�B �B(�A0�A8�Gp�8A0A(B BBB       L   �D  X����   B�B�B �B(�A0�A8�Gp�8A0A(B BBB       d   �D  �+��   B�B�B �B(�A0�A8�D@�
8A0A(B BBBGD8C0A(B BBB       ,   \E  p���=    B�D�A �rAB       d   �E  �����   B�B�B �B(�A0�D8�DPY
8A0A(B BBBD�
8A0A(B BBBF  \   �E  �,���'   B�E�H �A(�A0�G�� 
0A(A BBBAH
0A(A BBBH    d   TF  H����   B�B�B �B(�D0�A8�DP�
8A0A(B BBBC�
8A0A(B BBBH  ,   �F  �S��
ABB   D   �E  �T���  s  B�B�E �A(�A0�D@I
0A(A BBBH  T   F  8���E	  C  B�E�B �B(�D0�A8�D��
8D0A(B BBBF       l   \F  V���  �  B�B�B �B(�A0�A8�D��
8A0A(B BBBC#
8A0A(B BBBC      T   �F  PZ���  y  B�B�B �B(�A0�D8�G�r
8D0A(B BBBE       T   $G  �]���  O  B�B�B �B(�A0�D8�G��
8D0A(B BBBE       T   |G  0b���  +  B�B�B �B(�A0�D8�G�
8D0A(B BBBE       T   �G  �f��O    B�B�B �B(�A0�A8�D��
8D0A(B BBBE       T   ,H  �k��B  �  B�B�B �B(�A0�A8�G�H
8A0A(B BBBA       d   �H  ���  s  B�B�F �B(�D0�D8�DP�
8D0A(B BBBGg
8A0A(B BBBGd   J  0��'   B�B�B �B(�A0�A8�GPe
8D0A(B BBBM�
8A0A(B BBBB    D   TI  ����e  �  B�B�B �A(�A0�Dp�
0A(A BBBH \   �I  Ѓ��x  �  B�B�B �D(�A0�GP�
0A(A BBBDm
0A(A BBBA    l   ,K  P��x   B�B�F �D(�A0�G@F
0A(A BBBDv
0A(A BBBJD
0F(A BBBG   D   lJ  `��c  �  B�B�J �A(�D0��
(A BBEA       ,   �J  �	���   �  A�D�G0x
AAA 4   L  
���    B�D�A �w
AEA{
AFIL   K  Ѓ���  u  B�B�B �B(�A0�A8�J��
8A0A(B BBBFL   lK  @
���   �  B�B�E �E(�D0�D8�DP�
8D0A(B BBBA T   �K  �
��  �  B�E�B �B(�A0�D8�G��
8A0A(B BBBA       l   L  x���k  �  B�B�B �B(�A0�D8�D��
8A0A(B BBBD�
8A0A(B BBBE       T   �L  8��(  7  B�H�B �B(�A0�A8�G�n
8A0A(B BBBA       L   N   ����   B�B�B �B(�A0�A8�G��
8A0A(B BBBJ   D   ,M  ���'  �  B�E�E �A(�A0�D@�
0D(A BBBA T   tM  ���w  �  B�B�B �B(�D0�A8�J�
8A0A(B BBBA       T   �M   ����  �  B�B�B �B(�A0�A8�G��
8A0A(B BBBA       T   $N  �����  �  B�B�B �B(�A0�A8�G�%
8D0A(B BBBE       L   |N   ��"  �  B�F�F �F(�D0�D8�G`i
8A0A(B BBBA <   �N   ��  �  B�E�D �D(�G@h
(D ABBA T   O  ����  {  B�B�E �B(�A0�A8�G��
8A0A(B BBBG       L   dO  h%���  �  B�B�E �E(�A0�A8�Dp�
8D0A(B BBBCT   �O  (��F  �  B�E�E �E(�A0�A8�G��
8A0A(B BBBF       T   P  ����n  �  B�B�B �B(�A0�A8�G��
8A0A(B BBBC       L   �Q  ����6   B�B�A �A(�D0C
(A ABBE�
(D ABBF  L   �Q  ����i   B�B�B �B(�A0�A8�G�M8A0A(B BBB      \   4R  ����   U�B�B �B(�A0�A8�G��8A�0A�(B� B�B�B�I�������,   �R  x����   A�A�D >
AAA    �   �Q  ص���  U  B�B�B �B(�D0�A8�D`b
8C0A(B BBBA
8A0A(B BBBH�
8C0A(B BBBD       <   R   ����  �  B�B�A �A(�DPo
(A ABBA $   �S  ����   A�K �A          �S  �1��              �S  �1��              �S  X���'           L   �S  p1���    B�I�A �A(�D0�
(A ABBGd(C ABB         LT  �1��              dT  ���'              |T  �1��L              �T  2��[             �T  P4��&              �T  h4��              �T  `4��              �T  X4��              U  P4��              $U  H4��           <   <U  @4��b    A�C�H d
CADI
CAKDHA    |U  p4��              �U  h4��           $   �U  p4��c    A�E�F MDA   �U  �4���    H�m
KU$   �U  85��F    DO
MW
AI        V  `5���    lQ L   4V  86���   D}
G@
HJ
Fd
LM
AX
Jg
I[
ER
E    L   �V  �7���   Dv
FQ
GC
E�
En
BW
I{
E�
EK
E   D   �V  H:��    A�C
HY
G_
I�
Eb
F    ����  ��" ��	    ��
3� �  ���� �  ��
K� �  ��
0� �  ��<�  ���
� ��
� �
� �  �� �  �� ��#�  �"� �� �� �� ��  ����  �� �	�  ��A  �� �� ��� �     ��!�  ���� �          ��1� �� ���
�
_� �
�����  ��
�)�
 ����	��
 �  ��	�
�
  ������
� �  �
3� ��  ��*!W n  ��  "1 H  ��  & =  ��
YX� �<  ��N�  ��
�. �
�. �
�. ��- ��& ��  ��1 �  ��+ �  ��1 ��1 ��1 ��0 �(�/ ��/ ��/ ��/ ��/ ��/ ��/ ��, ��, ��+ ��- ���+ ��  ��& ��' ��' ��' ���& ��* ��* ��) ��) ��) ��) ��( ��' ��- ��, � �+ �!3  �!�* �!�  �#�& �$�  �+|  �0  �1r  ����
�  ���)� ��
��
�� ����	�
� �  �  �� �� �� �
 ���	 �	  ����  ���' ��/ ���' ���/ �	�. �	��/ �_�- �
� �
� �� �� ��� �-  �� ��  ��� �� �� �� �� �� �� �� �� �� �� �� �� �  �   ������ �       ��91lh  �^� �  �������  �� �       ���\  �� �
�� �
 ��  ��	 ��	 �P  ��
 �	�  ��[9  ��
  �
4� �  �� �  �
 �  ��
��������
 �
  ��
       ��                            0)            P                           ��            k            �V      	              ���o    �j     ���o           ���o    �]     ���o    7                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               H')                     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �     �     &�     6�     F�     V�     f�     v�     ��     ��     ��     ��     ��     ��     ��     ��     �                             �H             ��             8�             �	             �	             �	             �	             �	             �	             
             �     
               �     �     
              �     �     
              �     �     
              �     �     
              �     �     
              �     �     
              �     �                   �     ����            ����            ����              �?            ����            ����                           U)             R             i             {             �             �             �             �             �             �          �             �     �                               �	     %     :             P     `     q                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     GCC: (GNU) 4.8.5 20150623 (Red Hat 4.8.5-11)  .symtab .strtab .shstrtab .note.gnu.build-id .gnu.hash .dynsym .dynstr .gnu.version .gnu.version_r .rela.dyn .rela.plt .init .text .fini .rodata .eh_frame_hdr .eh_frame .gcc_except_table .init_array .fini_array .jcr .data.rel.ro .dynamic .got .got.plt .data .bss .comment                                 �                    �                    X&                    H�                    �]                   �j                   k                   ��                  	 ��                  
  �                   �                   `G                  
	                   �")                   �")                   �")                    #)                   H')                   ())                    0)                   �:)                   �N)                                       ��                     �            K     �     $      h    
   
    �T)            y
   
   
    U)            &    �T)            V    �T)            �   
	             �    �")                  ��                �    �:)            �     #)                 H')                 hN)                  0)             3    �w     �       X    PT)     0       _    Y     v       �                     �    H)            �    �=)            �    (U)            �  "  ��     I          �K)            $                     .                     ;    �J)            ]                     o                     {    �=     �       �    xC)            �    �P)     `       �  "   �            �    �     2       �                         �F)                                 8    �:)            E                     Q                     �    �=)            �                     �                     �  "  p     �           X     �       =    �>)            T                     _    0}     G      {                     �                     �                     �                     �    �C)            �    �     C       �    �@)                                       ��            3                      C                      R                      f     8B)            ~     �M)            �   "  ��     �      �     �I)            !    �J)            7!    H>)            M!    h�)            m!    �<)            y!                     �!  "  p�     �      �!    �     2       �!    �?)            "                     "                     +"                     A"                     N"    �F)            d"    �K)            �"    �I)            �"    PI)            �"    �#           �"                     �"    �     W       �"                     #  "  �     �       !#                     8#                     H#     F)            _#                     s#                     �#                     �#    ��     "       �#                     �#                     �#    �A)            �#    ��     +      �#                     $    D)            $    ��           ?$                     L$     =)            ]$    I)            |$                      �$                     �$    pM)            �$    @)            �$     �           �$                     %    Pt     '       $%                     0%  "  ��            L%    �I)            f%                     r%    ��     	       �%                     �%    �%     �'      �%                     �%    xI)            &    �=)            &                     %&    H)            7&    �@)            P&     P)     `       v&                     &                     �&                     �&    �G)            �&    0N)            �&                     �&    p�            �&                      '                     '                     !'    �H)            A'    �@)            Y'    XA)            r'     A)            �'    `K)            �'    �T)            �'    �-     �      3(    �@)            L(                     [(                     k(    �M)            �(    �@)            �(                     �(    �E)            �(    (N)            �(                     �(    �K)            )    �     2       )  !  @#)     �       *)  "   �            F)                     S)                     a)                     �)    0�     �       �)                     �)    �F)            �)                     �)     J)            �)                      *    �=)             *    (@)            7*     �            K*                     Y*                     �*    @A)            �*  "  @�     �      +     @)            )+  "  `     '      {+    �F)            �+                     �+    xG)            �+    �C)            �+    `A)            �+    p�     Q      ,    @�     ?      ,                     +,    �-     �      U,                     �,    HG)            �,    0�     2      �,    �N)            �,                     �,                     �,                     	-    �N)            -                     "-    XN)            <-                     C-                     M-    �H)            l-                     y-    ��     �      �-                     �-    PK)            �-    �F)            �-     �     i      �-    �L     �      ?.    �?     �      L.    �B)            c.    �D)            �.                     �.                     �.     C)            �.    M)            �.                     �.    �<)            �.                     #/    �G)            8/                     M/                     W/    F)            s/    8E)            �/                     �/  "  �     =       �/    hA)            �/                     �/                     	0    ��            %0    �A)            =0  "  ��           R0    �D)            l0  ! 
       �>  "  0�     M       ?                     ?                     ,?    P5     R      �?    �A     �      �?                     �?    �M     
K    ��     �       K                     2K    �~           OK                     [K    `=     c       oK    �K)            �K    �?)            �K                 �K    �?)            �K                      L                     
S                     S    `�     �       S    �H)            @S                     QS    �I)            kS                     }S    �C)            �S    �G)            �S    p<            �S                     �S    ��     	      �S    �f     �      (T                     2T                     >T    ��     �      \T    P�     �      sT    0     n      �T                     �T     E)            �T    �C)            �T                     �T                     �T    �C)            U     J)            -U                     ?U    C)            PU                     \U    �     8       zU    �K)            �U    �C)            �U    HD)            �U    @�     �      �U    `H)            	V     R)            ,V    �L            PV    �O)            aV    @E)            uV    ��     C	      �V  "   �            �V                     �V    8>)            �V    �     n      �V    �I)            �V     I)            W    �K)            7W     O)            HW    �I)            cW                     pW    x>)            �W                     �W    0�     T       �W    �T)            �W  !   $)     �       �W                     �W    ��            �W                     X    �:)            X    Q     d      DX  "  0�     �      �X  "  ��     �       �X    0O)            �X                     �X    p/           �X    �L)            Y    �=)            Y    `�)            7Y                     =Y    HB)            TY                     jY    G)            ~Y                     �Y                     �Y    `     �      �Y                     �Y                     �Y    ��     i      �Y                     Z    �B)            #Z    @R)            .Z  !  #)            <Z    �G)            SZ  "  ��     v       �Z  "  ��            �Z    �	     {      �Z                     �Z    (C)            �Z    �=)            �Z                     	[                     [    `<)            ([    �G)            =[                 Z[                     j[                     v[                     �[                     �[    ��     ~      �[                     �[  "  ��            �[                     �[    �D)            \    8K)            ;\    I)            [\  "  0�     l       {\    Pi     ,      �\    �H)            �\    ��     ~      �\                     ]                     ]    �I)            ,]                     8]                     I]    �E)            d]    (F)            }]                     �]                     �]                     �]                     �]                     �]                     �]     >           ^    �A)            ^                     )^    @F)            H^    `I)            h^                     |^    @J)            �^                     �^    p�     �      �^    ��     \      �^                     �^    @I)            _                     )_                     V_  !  �$)            o_                     }_                     �_    ��     �       �_    У     :       �_                     �_    pH)            �_     �           �_    C)            `                     `                     %`    B)            ;`    B)            Q`    ��           m`  "  ��     E	      �`    8M)            �`    @H)            �`                     �`    �@)            �`    p-     �      a    D)            a                     *a                     9a                     La    `C)            aa    �x     �      a                     �a                     �a    @�     (       �a    H�)            �a                     b                     b    8G)            .b                     <b    0�     �       Yb                     nb    X>)            �b    �=)            �b                     �b  "  ��     L      �b                     �b                     �b    p�     (       �b  "  P�     A      Rc  "  p�            kc    ��           �c    ��     �      �c                     �c                     �c                     �c                     �c                     �c                     d    �H)            "d                     2d                     Fd    �=)            \d    �H)            ~d    (?)            �d    N)            �d    �B)            �d    @M)            �d    Љ     G       e                     e    ��            &e                     9e     U)            Te                     ae    xJ)            �e  "  ��            �e    �G)            �e    xF)            �e    �Q)            �e  "  �     x      uf                     �f  "  ��     �      �f  "   
     �       ?g                     Hg    J)            bg    ��            pg    ��     6      �g                     �g    �             h                     "h  !  �%)     �       3h    �G)            Jh                     Rh    �     �      Yh    �K)            th                     �h    PB)            �h     ;)            �h    �D)            �h                     �h    (E)            �h    P�     V       	i                     i                     )i    8?)            Ei    �<            Zi                     fi                     qi    �<)            �i    �L)            �i                     �i     �     �       �i    �<)            �i    ��     k      �i    @f     M       j                     $j                     2j                     >j    �     u      Zj    �>)            oj                     |j    �/     �      �j                      �j    ��     +       k    @J     z      k  "  0�            3k                     Ck                     Gk                     Tk    p     1
       |    M)            :|                     H|    �A)            b|    `      w       �|     N)            �|                     �|                     �|    @K)            �|    �K)            �|    �M)            }    p�     (       +}                     ?}    �>     �       L}    �H)            l}    �J)            �}    �J)            �}    �J)            �}                     �}    �     �       �}    �            ~     V           3~                     E~    �9     
�                     �    �B)            0�                     6�    xL)            P�    O)            i�                     v�                     ��    pG)            ��                     ��    �     :      ƅ                     ԅ                     �                     �                     ��    �<     b       �                     $�    �K)            <�                     I�    �C)            [�  "  ��           p�  ! 
      �    �D)            �                     )�                     6�    �D)            N�    �F)            g�                     s�                     �    �A)            ��    @?)            ��                     ��                         �            ݊    G)            �                     ��                     9�    XG)            N�                     _�    PM)            {�                     ��    �=)            ��    �?)            ��                     ы                     ��                     �    0K)            �    �G)            &�    �T)            5�    0G)            J�    hN)             V�    B)            q�    �O)            ��    �     "       ��    x@)            ��    8H)            ǌ                     ӌ    �B)            �    �@)             �                     �    �L)            ,�                     =�                     N�    p�     R       c�                     p�    I)            ��                     Ǎ    @�     �       �                     ��  "  p�     �      ]�    8@)            t�                     �    �N)            ��                     ��    @B)            ��    0H)            ߎ  "  ��     �       �                     �     >)            )�    �      m      ;�     �     \      \�    �M)            v�    �K)            ��                     ��                     ��                     Տ  "  0�     M      M�    `�     �      X�    ?)            m�     L)            ��                     ��  "  ��     �      ̐     �     �       �    @Q     ]      �    pB)            %�                     0�    �O     �      R�    �.     z       m�    �@)            ~�    �>)            ��                     ��    �N     ,      Ց  "  ��     v       
�    �E)            #�                     4�                     F�    PC)            ^�                     s�    �     5       ��    �M)            ��  "  �     �      ��    Б     �      �    hG)            �    @T)            (�                     4�    @O)            H�    �L)            g�    p?)            �    E)            ��                     ��    8I)            ��     N)            ܓ  "  ��     �       ��    �G)            �    �=)            �    ��     -       +�    xE)            =�    ��     5      \�    `F)            r�  "  P�     �          �     �       ߔ    @Q)     `        �                     �    xD)            $�    E)            :�    �     #      ��                     ��    �?)            ��    �     v       Õ                     �                     ��          @       
      �  "  @)     �      V�    �B)            l�                     z�    �            ��                     ��    Р     M       ��    �I)            е     R)            �                     ��                     �    �B)            "�                     *�                     >�    HC)            V�    h?)            m�                     ��    �I)            ��    �F)            ��    �D)            ̶                     ֶ     G)            �  "  P"     �      X�                     d�    �C)            |�    �o     �      ��                     ��  "   I     �      �     �            2�                     ��    xA)            ��                     ��                     ��    pO)            ϸ    A)            �    �F)            ��                     �  "  �E     �      x�    ��     '      ��    hF)            ��                     ù                     ӹ     ?)            �                     ��    8J)            #�                     .�    XC)            G�                     [�                     o�    (I)            ��                     ��    pF)            ��    @u     �      ں    �>)            �    �H)            �                     ,�                     7�     "            TwBar.cpp _ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc.part.11 _ZL9ClampTextRSsPK8CTexFonti _ZZN10CTwVarAtom9IncrementEiE19__PRETTY_FUNCTION__ _ZL13COLOR32_WHITE _ZL12COLOR32_ZERO _ZL13PopupCallbackPv _ZZN6CTwBar14DrawHierHandleEvE19__PRETTY_FUNCTION__ _ZZN6CTwBar8OpenHierEP11CTwVarGroupP6CTwVarE19__PRETTY_FUNCTION__ _ZZN6CTwBar10LineInHierEP11CTwVarGroupP6CTwVarE19__PRETTY_FUNCTION__ _ZZNK6CTwBar12RotoGetValueEvE19__PRETTY_FUNCTION__ _ZZN6CTwBar12RotoSetValueEdE19__PRETTY_FUNCTION__ _ZZNK6CTwBar10RotoGetMinEvE19__PRETTY_FUNCTION__ _ZZNK6CTwBar10RotoGetMaxEvE19__PRETTY_FUNCTION__ _ZZNK6CTwBar11RotoGetStepEvE19__PRETTY_FUNCTION__ _ZZN6CTwBar12CEditInPlaceC1EvE19__PRETTY_FUNCTION__ _ZZN6CTwBar12CEditInPlaceD1EvE19__PRETTY_FUNCTION__ _ZZN6CTwBar22EditInPlaceEraseSelectEvE19__PRETTY_FUNCTION__ _ZZN6CTwBar23EditInPlaceGetClipboardEPSsE19__PRETTY_FUNCTION__ _ZZNK10CTwVarAtom13ValueToStringEPSsE19__PRETTY_FUNCTION__ _ZZN6CTwBarC1EPKcE19__PRETTY_FUNCTION__ _ZGVZN6CTwBar10ListValuesERSt6vectorISsSaISsEERS0_IjSaIjEES6_PK8CTexFontiE7Summary _ZZN6CTwBar10ListValuesERSt6vectorISsSaISsEERS0_IjSaIjEES6_PK8CTexFontiE7Summary _ZL13COLOR32_BLACK _ZZN6CTwBar15BrowseHierarchyEPiiPK6CTwVariiE19__PRETTY_FUNCTION__ _ZZN6CTwBar6UpdateEvE19__PRETTY_FUNCTION__ _ZZN6CTwBar4DrawEiE19__PRETTY_FUNCTION__ _ZZN6CTwBar11MouseMotionEiiE19__PRETTY_FUNCTION__ _ZZN6CTwBar11MouseButtonE16ETwMouseButtonIDbiiE19__PRETTY_FUNCTION__ _ZZN6CTwBar10MouseWheelEiiiiE19__PRETTY_FUNCTION__ _ZZN6CTwBar7KeyTestEiiE19__PRETTY_FUNCTION__ _ZZN6CTwBar10KeyPressedEiiE19__PRETTY_FUNCTION__ _GLOBAL__sub_I_TwBar.cpp TwMgr.cpp __tcf_1 __tcf_0 __tcf_2 __tcf_3 _ZL16SynchroHierarchyP11CTwVarGroupPKS_ _ZL20InactiveErrorHandlerP9_XDisplayP11XErrorEvent _ZL13IgnoreXErrorsv _ZL18s_PrevErrorHandler _ZL14RestoreXErrorsv _ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc.part.12 _ZN6CTwMgr12PixmapCursorEi.part.27 _ZL9g_CurMask _ZL9g_CurPict _ZL8g_CurHot _ZL13ErrorPositionbii.isra.79 _ZNK13StructCompareclERK7ETwTypeS2_.part.127 _ZZNK13StructCompareclERK7ETwTypeS2_E19__PRETTY_FUNCTION__ _ZNSt4listIN6CTwMgr12CStructProxyESaIS1_EE9_M_insertESt14_List_iteratorIS1_ERKS1_.isra.368 _ZNSt6vectorIP6CTwVarSaIS1_EE6resizeEmS1_.constprop.505 _ZZN9CColorExt14CopyVarToExtCBEPKvPvjS2_E19__PRETTY_FUNCTION__ _ZZN9CColorExt16CopyVarFromExtCBEPvPKvjS0_E19__PRETTY_FUNCTION__ _ZZN14CQuaternionExt16CopyVarFromExtCBEPvPKvjS0_E19__PRETTY_FUNCTION__ _ZZN14CQuaternionExt14CopyVarToExtCBEPKvPvjS2_E19__PRETTY_FUNCTION__ _ZZN14CQuaternionExt6DrawCBEiiPvS0_P6CTwBarP11CTwVarGroupE19__PRETTY_FUNCTION__ _ZZN6CTwMgr16CClientStdString8ToClientEvE19__PRETTY_FUNCTION__ _ZZN6CTwMgr12CCDStdString5SetCBEPKvPvE13s_EmptyString _ZZN6CTwMgr13CLibStdString10FromClientERKSsE19__PRETTY_FUNCTION__ _ZZN6CTwMgr13CLibStdString5ToLibEvE19__PRETTY_FUNCTION__ _ZGVZN6CTwMgr12CCDStdString5GetCBEPvS1_E8s_LibStr _ZZN6CTwMgr12CCDStdString5GetCBEPvS1_E8s_LibStr _ZZN6CTwMgr12CCDStdString5GetCBEPvS1_E13s_EmptyString _ZZN6CTwMgr8MinimizeEP6CTwBarE19__PRETTY_FUNCTION__ _ZZN6CTwMgr8MaximizeEP6CTwBarE19__PRETTY_FUNCTION__ _ZZN6CTwMgr4HideEP6CTwBarE19__PRETTY_FUNCTION__ _ZZN6CTwMgr6UnhideEP6CTwBarE19__PRETTY_FUNCTION__ _ZZN6CTwMgr7SetFontEPK8CTexFontbE19__PRETTY_FUNCTION__ _ZL18TwFreeAsyncDrawingv _ZZ11TwDeleteBarE19__PRETTY_FUNCTION__ _ZZ14TwSetBottomBarE19__PRETTY_FUNCTION__ _ZZ11TwSetTopBarE19__PRETTY_FUNCTION__ _ZL12TwMouseEvent14ETwMouseAction16ETwMouseButtonIDiii _ZZ15BarVarHasAttribP6CTwBarP6CTwVarPKcPbE19__PRETTY_FUNCTION__ _ZZ15BarVarSetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiPKcE19__PRETTY_FUNCTION__ _ZZ12TwGetKeyCodePiS_PKcE19__PRETTY_FUNCTION__ _ZZ14TwGetKeyStringPSsiiE19__PRETTY_FUNCTION__ _ZL10KeyPressediib _ZZ6TwDrawE19__PRETTY_FUNCTION__ _ZZ15BarVarGetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEEE19__PRETTY_FUNCTION__ _ZZ8TwDefineE19__PRETTY_FUNCTION__ _ZL6AddVarP6CTwBarPKc7ETwTypePvbPFvPKvS4_EPFvS4_S4_EPFvS4_ES4_S2_ _ZZL6AddVarP6CTwBarPKc7ETwTypePvbPFvPKvS4_EPFvS4_S4_EPFvS4_ES4_S2_E17s_UnnamedVarCount _ZZL6AddVarP6CTwBarPKc7ETwTypePvbPFvPKvS4_EPFvS4_S4_EPFvS4_ES4_S2_E19__PRETTY_FUNCTION__ _ZL14s_SeparatorTag _ZL17InsertUsedStructsRSt3setI7ETwType13StructCompareSaIS0_EEPK11CTwVarGroup _ZZL17InsertUsedStructsRSt3setI7ETwType13StructCompareSaIS0_EEPK11CTwVarGroupE19__PRETTY_FUNCTION__ _ZL13CopyHierarchyP11CTwVarGroupPKS_ _ZL11SplitStringRSt6vectorISsSaISsEEPKciPK8CTexFont.constprop.501 _ZZL11SplitStringRSt6vectorISsSaISsEEPKciPK8CTexFontE19__PRETTY_FUNCTION__ _ZL16AppendHelpStringP11CTwVarGroupPKcii7ETwType.constprop.500 _ZZL16AppendHelpStringP11CTwVarGroupPKcii7ETwTypeE19__PRETTY_FUNCTION__ _ZL16AppendHelpStringP11CTwVarGroupPKcii7ETwType.constprop.498 _ZL16AppendHelpStringP11CTwVarGroupPKcii7ETwType _ZL10AppendHelpP11CTwVarGroupPKS_ii _ZZL10AppendHelpP11CTwVarGroupPKS_iiE19__PRETTY_FUNCTION__ _ZZ12TwDefineEnumE19__PRETTY_FUNCTION__ _ZZ14TwDefineStructE19__PRETTY_FUNCTION__ _ZL9TwInitMgrv _ZZL9TwInitMgrvE19__PRETTY_FUNCTION__ _ZZL13TwCreateGraph11ETwGraphAPIE19__PRETTY_FUNCTION__ _ZZ6TwInitE19__PRETTY_FUNCTION__ _GLOBAL__sub_I_TwMgr.cpp _ZL11s_PassProxy LoadOGL.cpp _GLOBAL__sub_I_LoadOGL.cpp LoadOGLCore.cpp _GLOBAL__sub_I_LoadOGLCore.cpp crtstuff.c __JCR_LIST__ deregister_tm_clones register_tm_clones __do_global_dtors_aux completed.6344 __do_global_dtors_aux_fini_array_entry frame_dummy __frame_dummy_init_array_entry TwColors.cpp TwFonts.cpp _ZZ14TwGenerateFontPKhiifE19__PRETTY_FUNCTION__ _ZL7s_Font0 _ZL9s_Font1AA _ZL9s_Font2AA _ZL12s_FontFixed1 _ZZ22TwGenerateDefaultFontsfE19__PRETTY_FUNCTION__ TwOpenGL.cpp _ZZN14CTwGraphOpenGL7EndDrawEvE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL8DrawLineEiiiijjbE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL8DrawRectEiiiijjjjE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL13DrawTrianglesEiPiPjN8ITwGraph4CullEE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL9BeginDrawEiiE23s_SupportTexRectChecked _ZZN14CTwGraphOpenGL9BeginDrawEiiE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL4ShutEvE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL8DrawTextEPviijjE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL13DeleteTextObjEPvE19__PRETTY_FUNCTION__ _ZZN14CTwGraphOpenGL9BuildTextEPvPKSsPjS3_iPK8CTexFontiiE19__PRETTY_FUNCTION__ TwOpenGLCore.cpp _ZZN18CTwGraphOpenGLCore9BeginDrawEiiE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore7EndDrawEvE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore8DrawLineEiiiijjbE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore8DrawRectEiiiijjjjE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore4ShutEvE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore13DeleteTextObjEPvE19__PRETTY_FUNCTION__ _ZN18CTwGraphOpenGLCore4InitEv.part.4 _ZZN18CTwGraphOpenGLCore13DrawTrianglesEiPiPjN8ITwGraph4CullEE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore8DrawTextEPviijjE19__PRETTY_FUNCTION__ _ZZN18CTwGraphOpenGLCore9BuildTextEPvPKSsPjS3_iPK8CTexFontiiE19__PRETTY_FUNCTION__ TwPrecomp.cpp TwEventGLFW.c TwEventGLUT.c CSWTCH.10 TwEventSDL.c TwEventSDL12.c s_WheelPos.2305 TwEventSDL13.c s_KeyMod.2368 s_WheelPos.2389 TwEventSFML.cpp _ZZ11TwEventSFMLE21s_PreventTextHandling _ZZ11TwEventSFMLE6s_KMod _ZZ11TwEventSFMLE10s_WheelPos TwEventX11.c s_KMod __FRAME_END__ __JCR_END__ DW.ref.__gxx_personality_v0 __dso_handle _DYNAMIC __TMC_END__ _GLOBAL_OFFSET_TABLE_ _ZN6CTwMgr12CCDStdString5GetCBEPvS1_ g_Wnds _ZN18CTwGraphOpenGLCore13DeleteTextObjEPv _ZNSs6resizeEmc _ZN2GL12_glBlendFuncE _ZN2GL11_glViewportE g_NbOGLFunc _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED2Ev _ZN6GLCore20_glGetQueryObjectuivE glColor3i glEvalPoint1 _ZN6GLCore20_glGetAttribLocationE glPopClientAttrib glUniform2i TwEventSpecialGLUT _ZN2GL10_glLightivE _ZN14CQuaternionExt14s_ArrowTriProjE _ZN18CTwGraphOpenGLCore8DrawRectEiiiij TwAddButton glColor3dv _ZN2GL16_glColorMaterialE _ZNSo9_M_insertImEERSoT_ g_ErrNotEnum XFetchBytes _ZNSt9basic_iosIcSt11char_traitsIcEE4initEPSt15basic_streambufIcS1_E _ZN2GL12_glVertex4ivE glVertexAttrib2s glLightfv _ZNSt6vectorIjSaIjEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPjS1_EEmRKj _ZN18CTwGraphOpenGLCore10NewTextObjEv _ZN2GL13_glTranslatefE glNormal3d _ZNK6CTwMgr9HasAttribEPKcPb _ZNSsC1EPKcmRKSaIcE glFogfv glVertexAttrib3fv glNormal3b _ZN2GL12_glIsEnabledE _ZN6CTwBar20EditInPlaceAcceptVarEPK10CTwVarAtom _ZN2GL8_glRectiE glRasterPos3f _ZN6CTwBar11NotUpToDateEv glActiveTexture glRasterPos2fv glDrawRangeElements _ZN2GL14_glPixelMapusvE _ZN6GLCore14_glStencilFuncE _ZNSt6vectorIPN6CTwMgr7CCustomESaIS2_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS2_S4_EERKS2_ _ZN6GLCore12_glUniform4fE _ZN6GLCore16_glCreateProgramE _ZN2GL12_glVertex3dvE _ZN6GLCore18_glBindVertexArrayE g_TabLength glVertex2s _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE8_M_eraseEPSt13_Rb_tree_nodeIS0_E _ZN8CTexFontD1Ev _ZN2GL14_glTexCoord3svE glDrawPixels glGenBuffers glStencilMaskSeparate glUniform1fv _ZN2GL12_glColorMaskE _ZN6GLCore19_glPointParameterivE _ZN6GLCore19_glUniformMatrix3fvE _ZN6GLCore18_glVertexAttrib1fvE TwTerminate glLightiv _ZNK6CTwBar10RotoGetMaxEv free@@GLIBC_2.2.5 _ZN6CTwMgr5CEnumC2ERKS0_ glCompressedTexImage3D glVertexPointer _ZN2GL13_glDrawPixelsE glGetProgramInfoLog glTexCoord3f glLineWidth TwCopyCDStringToClientFunc glUniform2iv glVertexAttrib2fv _ZN2GL15_glRasterPos2dvE _ZN6CTwBar11MouseMotionEii glPolygonStipple _ZN2GL9_glIndexiE _ZNK10CTwVarAtom13ValueToDoubleEv glTexCoord3d g_ErrNoBackQuote _ZN6GLCore17_glVertexAttrib2sE __pthread_key_create glVertex2i _ZN6GLCore14_glPixelStorefE _ZN2GL13_glTexCoord1sE _Z13TwSetBarStateP6CTwBar8ETwState glCopyTexImage1D _ZN6CTwMgr16CClientStdStringC2Ev glStencilOp _ZNK11CTwVarGroup7IsGroupEv _ZN6GLCore12_glUniform3fE glColor3usv _ZNK6CTwBar4FindEPKcPP11CTwVarGroupPi strcasecmp@@GLIBC_2.2.5 _ZN14CQuaternionExt11CreateArrowEv glRasterPos3fv _ZN6GLCore19_glUniformMatrix4fvE _ZN2GL12_glVertex4svE glBlendFunc _ZN2GL9_glBitmapE _ZN2GL15_glRasterPos4svE _ZN14CQuaternionExt15s_ArrowColLightE glRectfv glRasterPos4f glUniform2fv _ZN2GL8_glClearE _ZN6GLCore10_glScissorE _ZNSs6appendEmc _Z14TwHandleErrorsPFvPKcEi glGetShaderSource glRasterPos4d abort@@GLIBC_2.2.5 _ZN6GLCore18_glVertexAttrib4fvE _ZN2GL14_glRasterPos4sE _ZN2GL15_glRasterPos3dvE _ZN2GL14_glRasterPos4dE _ZN6GLCore13_glBufferDataE g_InitCopyCDStringToClient _ZNK6CTwVar9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZN2GL15_glRasterPos4ivE glGetClipPlane glColorMaterial _ZN6GLCore12_glColorMaskE _ZN2GL10_glRotatefE glGetPointerv _ZN2GL15_glEvalCoord2fvE _ZN6GLCore16_glTexParameterfE strncpy@@GLIBC_2.2.5 _ZN6GLCore16_glDeleteQueriesE TwAddVarCB _ZTV8ITwGraph _ZN18CTwGraphOpenGLCoreD2Ev glDrawArrays glTexCoord3dv _ZNSt18basic_stringstreamIcSt11char_traitsIcESaIcEED1Ev _ZN6CTwMgr12CStructProxyD2Ev glVertex3sv _ZN2GL12_glColor4uivE _ZdlPv _ZN6GLCore12_glUniform2fE _ITM_deregisterTMCloneTable g_ErrExist _ZN2GL13_glTexCoord1fE _ZN10CTwVarAtomD0Ev glTexCoord1iv _ZTTSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZN2GL14_glRasterPos3iE _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE8_M_eraseEPSt13_Rb_tree_nodeIS4_E _ZN2GL14_glTexCoord1svE _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr5CEnumES4_EET0_T_S6_S5_ _ZN2GL11_glColor4svE glNormal3dv _ZN2GL10_glColor3sE _ZN2GL11_glIndexubvE _ZN2GL14_glRasterPos3dE _ZN11CTwVarGroup12FindShortcutEiiPb _ZN6CTwBar10MouseWheelEiiii glTexCoord2dv _ZN9CColorExt16CopyVarFromExtCBEPvPKvjS0_ _ZSt18_Rb_tree_decrementPSt18_Rb_tree_node_base _ZN2GL11_glColor3usE _ZN6CTwBar18ComputeLabelsWidthEPK8CTexFont _glDisableVertexAttribArray glColor4ui _ZNSt8ios_baseD2Ev glTexGend g_DefaultLargeFont _Znam _ZN6GLCore12_glFrontFaceE sincos glColor3s _ZN6GLCore17_glVertexAttrib3sE glUniform4iv _ZN6CTwBar15RotoOnMouseMoveEii glVertex4iv _ZN6GLCore19_glGetBufferSubDataE _ZN2GL12_glColor4ubvE _ZN6CTwBar10KeyPressedEii _ZN6CTwBar10ListLabelsERSt6vectorISsSaISsEERS0_IjSaIjEES6_PbPK8CTexFontii TwEventSDL13 _ZN2GL13_glMaterialfvE _ZN2GL20_glGetPolygonStippleE glStencilFuncSeparate XLookupString _ZN2GL8_glMap2dE _ZN6GLCore20_glGetTexParameterfvE glGetMapdv g_ErrHasNoValue _ZSt18_Rb_tree_incrementPSt18_Rb_tree_node_base _ZN2GL11_glColor3bvE toupper@@GLIBC_2.2.5 glColor4b _ZN2GL18_glEdgeFlagPointerE _ZN2GL11_glGenListsE glPointParameterfv _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED1Ev _ZN2GL15_glRasterPos2svE XCreateFontCursor glGetTexImage _ZN6CTwBar11CRotoSliderC1Ev _ZN2GL14_glRasterPos2dE _ZN6CTwMgr5CEnumD2Ev _ZN2GL16_glGetPixelMapfvE _ZTS11CTwVarGroup glUniform1f _ZN2GL8_glFogfvE _ZN2GL13_glTexCoord2sE _ZN6CTwBar4ShowEP6CTwVar _ZN6GLCore23_glCompressedTexImage2DE glColor4f _ZN14CTwGraphOpenGL10NewTextObjEv _ZNSt6vectorIjSaIjEED1Ev _ZN11CTwVarGroupD1Ev glMaterialf TwEventMouseMotionGLUT _ZN2GL14_glStencilFuncE glTexGenf glClipPlane _ZN6GLCore15_glGetUniformivE _ZN2GL8_glRectsE _ZN6CTwBar6UpdateEv glClear glVertexAttrib1s _Z15BarVarSetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiPKc _ZN2GL12_glMaterialfE _ZN2GL12_glPopMatrixE _ZN2GL12_glLineWidthE glBufferData glBufferSubData glVertexAttrib4Nbv _ZN14CTwGraphOpenGL14ChangeViewportEiiiiii _ZN14CQuaternionExt9CopyToVarEv _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr13CStructMemberES4_EET0_T_S6_S5_ _ZN2GL8_glMap1dE _ZN2GL14_glTexCoord2ivE _ZN2GL12_glEvalMesh1E TwGetBarName _ZN2GL9_glRectdvE _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6GLCore13_glBeginQueryE _Z12UnloadOpenGLv glColor4d __cxa_end_catch _ZN18CTwGraphOpenGLCore9BeginDrawEii _ZN2GL12_glGetStringE _Z7__TwDbgPKci TwSetParam _ZN6GLCore13_glUniform2ivE _ZN2GL15_glSelectBufferE _ZN2GL8_glRectfE _ZN6GLCore12_glBlendFuncE _ZN6GLCore13_glClearColorE glColor3sv _ZN2GL9_glIndexdE glGetCompressedTexImage TwWindowSize glIndexd glCopyTexImage2D _ZN6CTwBar14DrawHierHandleEv _ZN6CTwBar23EditInPlaceSetClipboardERKSs _ZN6GLCore16_glDeleteBuffersE _ZN6GLCore17_glVertexAttrib1sE glIndexf _ZNSs7reserveEm _ZN14CQuaternionExt11InitDir3FCBEPvS0_ glTexCoord1s _ZN2GL12_glNormal3bvE _ZN6GLCore17_glGetProcAddressE g_ErrNotFound _ZN10CTwVarAtom15ValueFromDoubleEd glGetVertexAttribdv glTexCoord4dv glBlendFuncSeparate _ZN2GL12_glIsTextureE glFogf _Z14TwGetKeyStringPSsii _ZN2GL10_glPopNameE _ZNSt19basic_ostringstreamIcSt11char_traitsIcESaIcEED1Ev glColor4us _ZN6CTwMgr13CLibStdString5ToLibEv glCompressedTexSubImage1D _ZN2GL14_glTexCoord1dvE _ZN6GLCore19_glVertexAttrib4NubE _ZNK10CTwVarAtom7IsGroupEv _ZN6GLCore15_glGetProgramivE _ZN6GLCore14_glPixelStoreiE _ZSt18_Rb_tree_incrementPKSt18_Rb_tree_node_base _ZN6GLCore18_glPointParameteriE _ZN2GL6_glEndE _ZN2GL9_glEnableE _ZN2GL10_glTexGenfE glRectiv _ZN2GL14_glEvalCoord2fE _ZN2GL10_glColor4dE glClearIndex glPixelMapfv _ZN6CTwBar7KeyTestEii glPixelMapusv TW_MOUSE_NA _ZN6CTwMgr7CStruct14DefaultSummaryEPcmPKvPv _ZNKSs7compareEPKc _ZN2GL15_glClearStencilE _ZNK10CTwVarAtom18MinMaxStepToDoubleEPdS0_S0_ _edata glRectd TwSetBottomBar _ZN6CTwBar8OpenHierEP11CTwVarGroupP6CTwVar _ZN14CTwGraphOpenGL8DrawRectEiiiijjjj _ZN2GL13_glTexCoord4sE TwDraw _ZN6GLCore16_glPolygonOffsetE atan2 glUniformMatrix4fv glTexCoord2s g_InitCopyStdStringToClient _ZN2GL11_glVertex2fE _ZN14CQuaternionExt9SummaryCBEPcmPKvPv _ZN6GLCore22_glStencilFuncSeparateE _ZNSs9push_backEc _ZN6CTwBar18ComputeValuesWidthEPK8CTexFont _ZN2GL14_glRasterPos3fE glReadBuffer TwSetCurrentWindow _ZN14CQuaternionExt11s_ArrowNormE glTexCoord2i glIndexubv _ZN14CQuaternionExt20ConvertFromAxisAngleEv _ZN2GL10_glColor3dE _ZN2GL12_glVertex3ivE _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ glPointParameteriv g_DefaultNormalFont glVertexAttrib4uiv _ZN2GL13_glMaterialivE _ZN6GLCore12_glUniform2iE _ZN2GL12_glClipPlaneE _ZN2GL14_glTexCoord2svE _ZTVSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZN2GL9_glLightfE glSelectBuffer _glGetVertexAttribiv glSampleCoverage _ZN10CTwVarAtom11SetReadOnlyEb glEvalCoord1dv _ZN6CTwBar15RotoOnMButtonUpEii TwDefineEnum _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN9CColorExt13InitColor4FCBEPvS0_ _ZNSt8ios_baseC2Ev _ZN6GLCore19_glGetActiveUniformE glRasterPos4iv _ZTS8ITwGraph _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_PKS3_ glIsQuery glClearStencil _ZNK10CTwVarAtom9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE TwEventSFML glMatrixMode TwRemoveAllVars _ZN2GL16_glNormalPointerE _ZN2GL14_glGenTexturesE _ZNKSs13find_first_ofEPKcmm glGetActiveUniform _ZN6GLCore11_glEndQueryE _ZN6GLCore26_glEnableVertexAttribArrayE glVertex4fv TwEventMousePosGLFWcdecl _ZN9CColorExt9SummaryCBEPcmPKvPv glEndQuery _ZN14CTwGraphOpenGL9BeginDrawEii _ZTI11CTwVarGroup _ZTVSt9basic_iosIcSt11char_traitsIcEE _ZNSt6localeC1Ev glVertexAttrib4dv _ZN2GL18_glPopClientAttribE _ZNSt6vectorIfSaIfEED2Ev glVertexAttrib4Nusv _ZN2GL7_glFogfE _ZN14CQuaternionExt8s_SphColE glDeleteLists _ZN6CTwBarD2Ev _ZN6GLCore18_glVertexAttrib3svE glColor3uiv _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ES3_RKS6_ _ZN2GL8_glOrthoE _ZN2GL15_glIndexPointerE glTexCoord2iv _ZNSt6vectorIP6CTwVarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN2GL13_glClearColorE _Z10LoadOpenGLv _ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc glLightModelfv _fini _ZN2GL12_glFrontFaceE _ZN2GL7_glHintE glColor4fv strlen@@GLIBC_2.2.5 _ZN6GLCore21_glGetUniformLocationE _ZN6CTwMgr16CClientStdString8ToClientEv _ZN14CTwGraphOpenGLD1Ev _ZN9CColorExt7HLS2RGBEv glUniformMatrix3fv glColorPointer _ZN6GLCore20_glGetVertexAttribdvE _ZNSt22__uninitialized_fill_nILb0EE15__uninit_fill_nIPN6CTwMgr13CStructMemberEmS3_EEvT_T0_RKT1_ glEvalMesh2 _ZNK10CTwVarAtom10IsReadOnlyEv _ZNK6CTwBar10RotoGetMinEv _Z14ColorRGBToHLSiiiiPiS_S_ _ZN14CQuaternionExt11InitDir3DCBEPvS0_ glListBase _ZNSsC1ERKSs _ZTV11CTwVarGroup _ZN2GL10_glScissorE _ZN2GL14_glGetTexGendvE _ZN2GL12_glEdgeFlagvE _ZN6CTwMgr16CClientStdString7FromLibEPKc _ZN6GLCore16_glBufferSubDataE _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED2Ev glTexCoord1dv _ZTS6CTwVar glGetPolygonStipple _ZN6GLCore24_glGetCompressedTexImageE _ZN6CTwBar15RotoOnLButtonUpEii _ZN14CTwGraphOpenGL9BuildTextEPvPKSsPjS3_iPK8CTexFontii _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE7_M_copyEPKSt13_Rb_tree_nodeIS2_EPSA_ buff_sz _ZTI10CTwVarAtom g_ErrUnknownAttrib g_InitWndHeight glGetTexGendv _ZN2GL13_glShadeModelE _ZN2GL12_glNormal3ivE _ZN6GLCore16_glTexSubImage1DE _ZN2GL13_glGetLightfvE _ZN2GL8_glFogivE glRectdv _ZN2GL11_glColor4ivE TwEventKeyGLFW glScissor _ZN6GLCore11_glIsShaderE glUniform4i _ZN2GL17_glTexParameterfvE glCompressedTexImage1D glTexParameteri TW_MOUSE_WHEEL _ZN6GLCore18_glGenVertexArraysE _ZN6CTwBar12CEditInPlaceD2Ev _ZN2GL13_glTranslatedE _ZNSo9_M_insertIdEERSoT_ _ZN2GL14_glTexCoord1fvE _ZN6CTwMgr4HideEP6CTwBar _ZN6GLCore19_glVertexAttrib4usvE _ZN2GL13_glClearDepthE glUniform1iv glVertexAttrib3dv _ZN6CTwMgr5CEnumC1ERKS0_ glIndexub glVertex3d _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN6GLCore22_glVertexAttribPointerE glGetShaderiv _ZN2GL9_glRectsvE glDisableVertexAttribArray _ZN2GL12_glNormal3fvE _ZN6CTwBar12CEditInPlaceC1Ev glColor3fv glRasterPos3iv strchr@@GLIBC_2.2.5 _ZN6GLCore15_glCreateShaderE glVertexAttrib4Niv XFree _ZN10CTwVarAtomC2Ev glGetVertexAttribiv _ZN6CTwMgr8MinimizeEP6CTwBar glColor4ubv TwEventKeyboardGLUT _ZN6GLCore18_glPointParameterfE _ZN2GL14_glTexCoord3ivE _ZN14CQuaternionExt12MouseLeaveCBEPvS0_P6CTwBar _ZN2GL13_glTexCoord3fE glValidateProgram glTexImage2D glVertex3f _ZN6GLCore14_glStencilMaskE _ZNSs6insertEmPKcm glClearAccum g_ErrShut _ZN6GLCore20_glVertexAttrib4NusvE glVertexAttrib4s glGetUniformfv _ZNSt6vectorIbSaIbEE13_M_insert_auxESt13_Bit_iteratorb _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE16_M_insert_uniqueERKS7_ __cxa_guard_release glEnable _ZN2GL16_glTexSubImage1DE _ZN8ITwGraphD2Ev _ZN6GLCore16_glTexSubImage2DE glEnableClientState _Z15BarVarGetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZN2GL15_glEvalCoord1fvE _ZN6GLCore13_glUseProgramE _ZN2GL14_glStencilMaskE _ZN6GLCore20_glDrawRangeElementsE glTexCoord4iv snprintf@@GLIBC_2.2.5 glGetUniformiv glGetTexGeniv _ZN2GL11_glVertex2iE glVertexAttrib2dv _ZN6GLCore7_glHintE _ZNK6CTwBar11RotoGetStepEv glEdgeFlag _ZNSs6appendEPKcm _ZN6CTwMgr13CLibStdString10FromClientERKSs _ZN6CTwMgr12CMemberProxy5GetCBEPvS1_ g_ErrCStrParam _ZN2GL14_glRasterPos3sE _ZN6GLCore19_glUniformMatrix2fvE g_ErrIsDrawing _Z15BarVarHasAttribP6CTwBarP6CTwVarPKcPb glNormal3fv XFreePixmap _ZN6CTwBar4DrawEi gettimeofday@@GLIBC_2.2.5 glRasterPos2d _ZN6GLCore15_glDeleteShaderE glIndexPointer _ZN6CTwMgrC1E11ETwGraphAPIPvi __assert_fail@@GLIBC_2.2.5 _ZN2GL11_glTexGenfvE _ZN2GL14_glRasterPos4fE _ZN6GLCore14_glGetTexImageE _ZN6GLCore16_glTexSubImage3DE glUseProgram _ZN8CTexFontD2Ev TwEventMouseButtonGLFW _ZN2GL13_glTexCoord2fE _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE8_M_eraseEPSt13_Rb_tree_nodeIS2_E _ZN6CTwMgr13UpdateHelpBarEv _ZNSs12_M_leak_hardEv glRectf _ZN2GL13_glTexCoord3sE glUniform3iv g_DefaultFixed1Font _ZN6CTwBar10LineInHierEP11CTwVarGroupP6CTwVar glCompressedTexSubImage3D _ZN2GL14_glLineStippleE glLighti _ZN2GL16_glTexSubImage2DE acos _Z7DrawArciiiffj _ZN6GLCore18_glVertexAttrib3fvE glGetPixelMapusv _ZN6GLCore12_glUniform1iE glVertexAttrib1dv _ZN2GL10_glIndexsvE _ZN2GL10_glColor3iE TwEventMouseButtonGLFWcdecl glGetBooleanv _Z14ColorHLSToRGBiiiiPiS_S_ _ZN18CTwGraphOpenGLCore13DrawTrianglesEiPiPjN8ITwGraph4CullE glNewList glPixelZoom _ZN6CTwBar15EditInPlaceDrawEv TwDefineEnumFromString _Z14TwGenerateFontPKhiif memset@@GLIBC_2.2.5 _ZN2GL14_glGetIntegervE _ZN2GL15_glLightModelfvE _ZSt20__throw_out_of_rangePKc glPolygonOffset _ZN2GL9_glLightiE _ZN6GLCore14_glLinkProgramE _ZNSsC1EmcRKSaIcE _ZN2GL8_glMap2fE XStoreBytes _ZN14CTwGraphOpenGL7RestoreEv _ZN6GLCore13_glBindBufferE _ZN2GL10_glLightfvE _ZN2GL20_glGetTexParameterivE _ZNK10CTwVarAtom9HasAttribEPKcPb _ZN6GLCore19_glVertexAttrib4ubvE _ZN14CQuaternionExt12s_CustomTypeE _ZN18CTwGraphOpenGLCore9IsDrawingEv _glBindBufferARB _ZN2GL10_glFrustumE _ZN10CTwVarAtom9IncrementEi _ZN18CTwGraphOpenGLCoreD1Ev glOrtho _ZN2GL12_glVertex3fvE TwDefineStruct _ZN6GLCore13_glUniform4ivE _ZN6GLCore18_glVertexAttrib2fvE _ZN6GLCore20_glMultiDrawElementsE _glBlendEquation _ZN6GLCore13_glUniform3ivE glDrawBuffer _ZN2GL12_glVertex2fvE glTexCoord2fv _ZN6CTwVarC1Ev g_TwMgr _ZTV14CTwGraphOpenGL _ZNSs9_M_mutateEmmm _ZN6CTwBar11CRotoSliderC2Ev strncat@@GLIBC_2.2.5 g_ErrUnknownType _ZN14CQuaternionExt14CopyVarToExtCBEPKvPvjS2_ _ZNSt6vectorIfSaIfEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPfS1_EERKf _ZN6CTwVarD0Ev _glClientActiveTextureARB glIsTexture _ZN14CTwGraphOpenGL8DrawTextEPviijj _ZN6GLCore13_glDepthRangeE g_ErrBadParam _ZN6GLCore21_glDeleteVertexArraysE glEnd _ZN2GL13_glPixelMapfvE XCreateBitmapFromData _ZN2GL10_glColor4iE glPixelStorei glDisableClientState _ZN6CTwMgrD2Ev glBlendEquationSeparate glPointParameteri _ZN9CColorExt11CreateTypesEv glGetTexParameterfv _ZN2GL14_glMultMatrixfE g_ErrParse _ZTI8ITwGraph _ZN2GL13_glClearIndexE _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED1Ev _ZNSt6vectorIiSaIiEED1Ev _ZN6CTwBar21EditInPlaceKeyPressedEii glEvalCoord1fv _ZN2GL8_glMap1fE _ZN2GL11_glVertex4iE glTexCoord2sv glShaderSource TW_MOUSE_MOTION _ZN2GL11_glColor3ivE _ZN6CTwBar14EditInPlaceEndEb glTexSubImage3D glUniform3f XCreatePixmapCursor glBlendColor _ZN6CTwMgr13CreateCursorsEv glPushClientAttrib _ZNSt6vectorIcSaIcEED2Ev _ZNKSs16find_last_not_ofEPKcmm _ZN2GL14_glGetPointervE _ZN6GLCore23_glGetBufferParameterivE _ZN6GLCore18_glVertexAttrib2svE _ZNK11CTwVarGroup10IsReadOnlyEv _ZN18CTwGraphOpenGLCore8DrawTextEPviijj _ZN6GLCore19_glVertexAttrib4NsvE _ZN6CTwBar11MouseButtonE16ETwMouseButtonIDbii glVertexAttrib4fv glRasterPos2f _ZN6GLCore12_glUniform3iE glMapGrid2d glVertexAttrib3s _ZN2GL17_glFeedbackBufferE _ZN2GL15_glDrawElementsE glTexCoord3fv glVertex4f glNormalPointer glVertex4d _ZNSs6appendERKSs _ZNSs6assignEPKc _ZN14CQuaternionExt9ApplyQuatEPfS0_S0_fffffff _ZN2GL17_glPolygonStippleE glMapBuffer _ZN2GL21_glDisableClientStateE _ZN6GLCore18_glVertexAttrib1dvE memcmp@@GLIBC_2.2.5 _ZN6GLCore20_glGetVertexAttribivE glLightModelf _ZN6CTwMgr12CMemberProxy5SetCBEPKvPv _ZNK11CTwVarGroup9HasAttribEPKcPb glTexEnvf _ZN6GLCore18_glVertexAttrib1svE glMapGrid2f _ZTVSt15basic_streambufIcSt11char_traitsIcEE _ZTI18CTwGraphOpenGLCore glLineStipple glDepthFunc _ZN6CTwMgr12CStructProxyC1Ev TwGetBarCount glGetTexEnviv _ZN6GLCore17_glVertexAttrib4sE _ZN6CTwMgr11FreeCursorsEv _ZN2GL12_glMapGrid1dE glMap1d glVertex3dv _ZN2GL12_glPointSizeE _ZN2GL12_glPixelZoomE _ZNK6CTwBar9HasAttribEPKcPb _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EEaSERKS3_ _ZN6GLCore12_glGetFloatvE _ZN2GL8_glAccumE glCreateProgram _ZN2GL9_glRectivE _ZN14CTwGraphOpenGL4InitEv _ZN2GL10_glIndexfvE glColor4sv glGetProgramiv __cxa_pure_virtual _ZN2GL11_glListBaseE _ZN6CTwMgrC2E11ETwGraphAPIPvi glBindBuffer _ZTTSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZN6CTwMgr12CMemberProxyC2Ev _ZN6GLCore16_glIsVertexArrayE glIsList glGetUniformLocation _ZN2GL10_glColor4bE glPixelMapuiv _ZN6CTwMgr12CStructProxyD1Ev glPrioritizeTextures _ZN2GL12_glVertex2svE _ZN2GL11_glVertex4sE glMap1f _ZNSt6vectorIcSaIcEE6resizeEmc glNormal3iv glLoadMatrixd _ZN6CTwMgr12CMemberProxyD1Ev _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE16_M_insert_uniqueERKS2_ _ZNSt6vectorIfSaIfEED1Ev _Z14ColorHLSToRGBffffPfS_S_ _Z14ColorRGBToHLSffffPfS_S_ glVertexAttribPointer glEvalCoord2fv glGenQueries glColor3ub glLoadMatrixf strcmp@@GLIBC_2.2.5 _ZN6GLCore18_glVertexAttrib4bvE glGetMaterialfv glStencilOpSeparate _ZN2GL12_glVertex4fvE _ZN6GLCore20_glVertexAttrib4NubvE _ZN2GL11_glTexEnvfvE _ZN6GLCore17_glTexParameterivE _ZN2GL11_glNormal3dE _ZN6GLCore11_glGetErrorE _ZNK10CTwVarAtom4FindEPKcPP11CTwVarGroupPi glDeleteShader TwRemoveVar glVertexAttrib4usv _ZN2GL17_glGetProcAddressE glUniform3fv _ZN6GLCore19_glGetShaderInfoLogE _ZNSt6vectorIiSaIiEED2Ev _ZN2GL10_glColor3bE _ZN2GL11_glCullFaceE _ZN14CQuaternionExt13s_SphColLightE _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE29_M_get_insert_hint_unique_posESt23_Rb_tree_const_iteratorIS4_ERS1_ glTexCoord4i _ZNSt6vectorIP6CTwVarSaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr18CCDStdStringRecordES4_EET0_T_S6_S5_ glFinish _ZN6GLCore12_glUniform1fE TwMouseButton _Z17TwDefineStructExtPKcPK15CTwStructMemberjmmPFvPvS4_EPFvS4_PKvjS4_EPFvS8_S4_jS4_EPFvPcmS8_S4_ES4_S0_ glGetAttribLocation _ZN9CColorExt13InitColor3FCBEPvS0_ glGetTexParameteriv _ZTV10CTwVarAtom _ZN2GL13_glClearAccumE glAccum TwInit _ZN6GLCore13_glGetQueryivE glClearColor _ZN2GL14_glPassThroughE g_ErrNoValue _ZN2GL17_glGetPixelMapusvE fprintf@@GLIBC_2.2.5 _ZN2GL14_glGetBooleanvE _ZN6CTwBar12RotoSetValueEd glXGetCurrentDrawable _ZNSdD2Ev _ZN2GL18_glTexCoordPointerE TwEventCharGLFWcdecl glCallLists glTexEnviv g_InitWndWidth _ZN6GLCore12_glIsEnabledE glTexParameterfv _ZN6CTwBar21EditInPlaceIsReadOnlyEv g_ErrBadType _ZN6CTwMgr9SetAttribEiPKc _ZN18CTwGraphOpenGLCore4InitEv _ZNSsC1ERKSsmm glUnmapBuffer glPopMatrix _Z22TwGenerateDefaultFontsf _ZN2GL11_glVertex2dE glUniform4fv _ZNK11CTwVarGroup9GetAttribEiP6CTwBarPS_iRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE __gmon_start__ TwSetLastError _ZN6CTwBarD1Ev _ZN18CTwGraphOpenGLCoreD0Ev glBlendEquation sin glTexCoord4s _ZNK10CTwVarAtom13ValueToStringEPSs _ZN2GL14_glEvalCoord1fE _ZN6GLCore10_glLogicOpE _ZN2GL12_glMapGrid2dE glRasterPos4sv glTexGeniv _glEnableVertexAttribArray _ZN6GLCore10_glDisableE _ZN2GL15_glRasterPos3svE glLoadIdentity _ZN6GLCore19_glPointParameterfvE _ZN2GL12_glColor3uivE glNormal3s _ZN2GL13_glDrawBufferE _Z20TwDeleteDefaultFontsv _ZNSs6assignEPKcm _ZN2GL16_glGetMaterialfvE glPixelTransferi g_ErrNotInit g_ErrDelStruct glColor3b TwEventX11 memcpy@@GLIBC_2.14 glColor3f _ZN2GL20_glInterleavedArraysE glRasterPos2iv _ZTI14CTwGraphOpenGL _ZN2GL13_glCopyPixelsE glColor3d g_OGLCoreFuncRec _ZTV18CTwGraphOpenGLCore _ZNSt8__detail15_List_node_base9_M_unhookEv glGetQueryiv TwSetTopBar _ZN2GL11_glNormal3iE glScalef glUniform2f glFlush glTexCoord3iv _ZN2GL15_glEvalCoord2dvE _ZN2GL15_glRasterPos2fvE _ZN8ITwGraphD0Ev glDrawBuffers _ZN6GLCore15_glShaderSourceE _ZN14CTwGraphOpenGLD0Ev _ZN6GLCore14_glDrawBuffersE glEvalPoint2 _ZN6GLCore21_glBindAttribLocationE _ZN2GL10_glIndexivE _ZN6CTwVarD2Ev glColor4bv glInitNames _ZNSt6vectorISsSaISsEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPSsS1_EERKSs glNormal3i _ZN2GL12_glDepthMaskE glGetError glScaled glLogicOp g_SmallFontTexID _ZN6GLCore20_glCopyTexSubImage1DE _ZN2GL15_glArrayElementE glIndexfv _ZN18CTwGraphOpenGLCore15RestoreViewportEv _ZN6GLCore15_glDrawElementsE _ZNK10CTwVarAtom8IsCustomEv _ZN2GL11_glVertex4dE glLoadName _ZN6GLCore12_glUniform4iE glRasterPos3s glRenderMode glEvalCoord1f TwCopyCDStringToLibrary glCompileShader _ZN6GLCore15_glDetachShaderE _ZN6GLCore17_glVertexAttrib4fE _ZNSt6vectorIdSaIdEE9push_backERKd glVertexAttrib4Nsv g_OGLFuncRec glEvalCoord1d g_ErrBadFontHeight TwCopyStdStringToLibrary glXGetCurrentDisplay _ZTV6CTwVar _ZN2GL10_glIndexdvE _ZN6GLCore16_glBlendEquationE _ZN6GLCore14_glPolygonModeE _ZN2GL13_glTexCoord3dE _ZN10CTwVarAtomD2Ev _ZN6GLCore13_glDrawBufferE glRasterPos3i _ZN6GLCore13_glGenQueriesE _ZN2GL13_glGetLightivE _ZN2GL13_glTexCoord2iE _ZN6GLCore20_glGetVertexAttribfvE _ZN6GLCore15_glClearStencilE glStencilMask _ZN6GLCore14_glGetPointervE glColor3bv _ZN2GL17_glCopyTexImage1DE _ZTVSt15basic_stringbufIcSt11char_traitsIcESaIcEE glCompressedTexSubImage2D _ZN6GLCore12_glDepthMaskE _ZN2GL14_glPixelStorefE _ZN2GL11_glLoadNameE TwGetCurrentWindow TwMouseMotion _ZN6GLCore14_glBindTextureE _ZN2GL14_glTexCoord3dvE _ZNSs4_Rep10_M_disposeERKSaIcE _ZN2GL11_glGetMapfvE _ZN2GL13_glEvalPoint2E glIsEnabled g_ErrOffset _ZN2GL12_glColor4usvE _ZN2GL11_glVertex3dE _ZN10CTwVarAtom9SetAttribEiPKcP6CTwBarP11CTwVarGroupi glRasterPos2dv XSetErrorHandler _ZN2GL14_glBindTextureE _ZN6GLCore23_glCompressedTexImage3DE _ZN6GLCore26_glCompressedTexSubImage1DE _ZN6GLCore18_glGetActiveAttribE glPushMatrix glReadPixels _ZN2GL11_glTexGenivE g_BarTimer _ZN2GL12_glIndexMaskE _ZN8ITwGraphD1Ev _ZN2GL12_glEvalMesh2E _ZN2GL14_glGetTexEnvivE _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE8_M_eraseEPSt13_Rb_tree_nodeIS7_E _glBindVertexArray _ZN6CTwMgr8MaximizeEP6CTwBar TwGetBarByIndex _ZN6GLCore18_glGetShaderSourceE _ZN6GLCore12_glMapBufferE glVertexAttrib2f glGetVertexAttribPointerv glVertexAttrib4Nuiv TwAddVarRW _ZN2GL14_glTexCoord4fvE glBitmap _ZN6GLCore22_glStencilMaskSeparateE glIndexsv glVertexAttrib2d glVertexAttrib1fv glMultiDrawElements _ZN2GL13_glRenderModeE _ZN14CTwGraphOpenGL13DeleteTextObjEPv _ZN2GL17_glCopyTexImage2DE _ZN6GLCore19_glVertexAttrib4uivE _ZN6CTwMgr19SetCurrentDbgParamsEPKci _ZN9CColorExt7RGB2HLSEv glCallList _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_S8_ TwAddVarRO glTexCoord3i g_NbOGLCoreFunc _ZN6GLCore20_glStencilOpSeparateE glLightModeliv _ZNK6CTwMgr9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNK5CRect8SubtractERKS_RSt6vectorIS_SaIS_EE _ZdaPv glVertex2iv _ZNSt6vectorIiSaIiEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPiS1_EERKi _ZN6CTwMgr13CLibStdStringC1Ev _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE16_M_insert_uniqueERKS0_ _ZN8CTexFontC2Ev _ZN6CTwBar17RotoOnMButtonDownEii _ZN2GL14_glTexCoord2dvE _ZN6GLCore13_glTexImage1DE _ZN6GLCore26_glCompressedTexSubImage2DE _ZN2GL14_glRasterPos2sE glAreTexturesResident glRasterPos3dv _ZN6GLCore19_glVertexAttrib4NivE glFrustum _ZN6GLCore16_glActiveTextureE glGetString TwKeyTest _ZN6GLCore25_glGetTexLevelParameterfvE glPixelStoref _ZN2GL16_glPolygonOffsetE _ZN9CColorExt13InitColor32CBEPvS0_ _ZN6GLCore17_glTexParameterfvE glTexCoord3s glColor4ub _ZN6GLCore14_glUnmapBufferE _ZN6GLCore19_glGetQueryObjectivE _ZN6GLCore8_glClearE _ZN6CTwMgr12CMemberProxyD2Ev glCopyTexSubImage3D TwEventSDL12 _ZN6GLCore18_glVertexAttrib4dvE _ZN6GLCore27_glDisableVertexAttribArrayE _ZN6GLCore14_glGetShaderivE _ZN6GLCore16_glDeleteProgramE glMultMatrixd _ZN6CTwMgr9SetCursorEm _ZN14CTwGraphOpenGL9IsDrawingEv _ZN18CTwGraphOpenGLCore4ShutEv glVertexAttrib4iv _ZN14CQuaternionExt16CopyVarFromExtCBEPvPKvjS0_ _ZN6GLCore14_glGenTexturesE _ZNSt6vectorIjSaIjEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPjS1_EERKj _ZN2GL13_glDepthRangeE _ZN6GLCore20_glCopyTexSubImage3DE _ZN6GLCore12_glDepthFuncE _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ glIndexiv _ZN2GL11_glGetMapdvE TwEventSDL sscanf@@GLIBC_2.2.5 _glBindProgramARB _end _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED1Ev sqrtf _ZN6CTwBar20EditInPlaceMouseMoveEiib _ZN2GL12_glColor3usvE _ZN6GLCore17_glVertexAttrib1fE _ZN6CTwBarC2EPKc _ZN6CTwMgr13CStructMemberD1Ev _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED0Ev TwDefine _ZN10CTwVarAtomC1Ev TwKeyPressed glTexEnvfv glDepthMask _ZN6CTwMgr7CStructD1Ev _ZN2GL12_glNormal3svE _ZN6GLCore26_glCompressedTexSubImage3DE glTexGeni _ZN2GL13_glTexCoord4dE _ZN6GLCore15_glAttachShaderE _ZN2GL13_glReadPixelsE glTexSubImage1D _ZTI6CTwVar _ZN2GL13_glTexImage2DE glBeginQuery _ZNK5CRect8SubtractERKSt6vectorIS_SaIS_EERS2_ _ZN6GLCore20_glBlendFuncSeparateE _ZN6GLCore20_glGetTexParameterivE _ZN2GL20_glCopyTexSubImage2DE _ZN2GL11_glNormal3fE _Z12IsCustomTypei _ZN6CTwMgr17UnrollCDStdStringERSt6vectorINS_18CCDStdStringRecordESaIS1_EE7ETwTypePv g_LargeFontTexID glUniform1i TwDeleteAllBars _ZN6GLCore20_glCopyTexSubImage2DE _ZN14CTwGraphOpenGL8DrawLineEiiiijb _ZN6CTwBar4FindEPKcPP11CTwVarGroupPi glGetActiveAttrib glColor4i _ZSt7getlineIcSt11char_traitsIcESaIcEERSt13basic_istreamIT_T0_ES7_RSbIS4_S5_T1_ES4_ glHint glDeleteQueries _ZN18CTwGraphOpenGLCore9BuildTextEPvPKSsPjS3_iPK8CTexFontii glRasterPos4s TwGLUTModifiersFunc glVertex2d glGetQueryObjectuiv _ZN6GLCore13_glUniform1fvE __gxx_personality_v0 glFogi _ZN6CTwMgr7SetFontEPK8CTexFontb glMultMatrixf _ZN2GL14_glMultMatrixdE XSync _ZN6GLCore12_glIsTextureE _glBlendEquationSeparate glTexCoord1d glEdgeFlagPointer _ZN2GL11_glColor3svE glRasterPos4i _ZN6CTwBar12UpdateColorsEv glTexCoord3sv glEvalCoord2dv glVertex3s glTexImage1D TwEventMouseButtonGLUT glTexCoordPointer _ZN6GLCore10_glIsQueryE glTexCoord1f _ZN2GL9_glIsListE _ZN6CTwMgr5CEnumD1Ev _ZTS10CTwVarAtom _ZN2GL16_glGetMaterialivE _ZN2GL15_glRasterPos3ivE glVertex2f _ZN6GLCore13_glTexImage3DE g_ErrInvalidAttrib _ZNSt6vectorIcSaIcEED1Ev TwEventCharGLFW glCompressedTexImage2D glMateriali _ZN6GLCore13_glGenBuffersE g_ErrDelHelp _ZN2GL11_glColor4uiE _glUseProgramObjectARB _ZN2GL9_glScalefE _ZN6GLCore18_glValidateProgramE _ZN2GL11_glVertex3iE _ZN2GL12_glMapGrid1fE _ZN14CQuaternionExt12InitQuat4DCBEPvS0_ glColor4usv _ZN2GL14_glPixelStoreiE _ZNSt6vectorIcSaIcEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPcS1_EEmRKc glMultiDrawArrays _ZNSt9basic_iosIcSt11char_traitsIcEE5clearESt12_Ios_Iostate glGenLists _Z10ParseTokenRSsPKcRiS2_bbcc glGetBufferParameteriv glIndexi _ZNSt6vectorIP6CTwBarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ glTexCoord2f _ZN6GLCore13_glReadPixelsE glIndexs XSetSelectionOwner _ZN2GL20_glCopyTexSubImage1DE _ZN11CTwVarGroup9SetAttribEiPKcP6CTwBarPS_i _ZN2GL11_glGetMapivE glStencilFunc glTexCoord2d _ZN2GL14_glGetTexEnvfvE _ZN2GL15_glColorPointerE glVertex2fv glPointSize _ZN2GL11_glPushNameE _ZN2GL14_glTexCoord4svE glRects XDefineCursor _ZN6CTwMgr12GetLastErrorEv _ZN2GL11_glColor4fvE glColor4s _ZSt28_Rb_tree_rebalance_for_erasePSt18_Rb_tree_node_baseRS_ _ZN2GL11_glColor3uiE XAllocNamedColor _ZN6GLCore14_glGetBooleanvE __cxa_begin_catch _ZN2GL16_glVertexPointerE _ZN2GL13_glTexCoord2dE _ZNSsC1EPKcRKSaIcE glRasterPos4dv __cxa_rethrow _ZN6GLCore20_glGetBufferPointervE _ZN2GL12_glCallListsE g_ErrorHandler _ZN2GL11_glColor4bvE __bss_start _ZN2GL17_glPixelTransferiE g_NormalFontTexID TwCopyStdStringToClientFunc _ZN2GL9_glScaledE _ZN2GL12_glAlphaFuncE glFrontFace _ZN2GL12_glMapGrid2fE _ZN2GL13_glReadBufferE glVertexAttrib1f _ZN6GLCore13_glDrawArraysE _ZNSt6localeD1Ev glVertexAttrib1d _Z13TwGlobalErrorPKc glGetLightfv _ZN6GLCore17_glVertexAttrib3dE _ZNKSt15basic_stringbufIcSt11char_traitsIcESaIcEE3strEv _ZNK11CTwVarGroup4FindEPKcPPS_Pi glDeleteBuffers _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN2GL13_glTexCoord1dE glTexGendv g_DefaultSmallFont glBindAttribLocation _ZN2GL14_glPixelMapuivE _ZN2GL22_glAreTexturesResidentE _ZN14CQuaternionExt7PermuteEPfS0_S0_fff glTexCoord1i _ZN2GL11_glVertex3sE _Z10ColorBlendjjf _ZN6CTwVar11GetDataSizeE7ETwType _ZN6GLCore12_glStencilOpE _ZN6GLCore18_glMultiDrawArraysE glGetTexLevelParameteriv glVertex3fv glEnableVertexAttribArray _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE8_M_eraseEPSt13_Rb_tree_nodeIS6_E TwGetParam _ZN2GL11_glTexGendvE _ZN6GLCore23_glCompressedTexImage1DE glRasterPos3sv _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED1Ev _ZN6CTwMgr6UnhideEP6CTwBar _ZN18CTwGraphOpenGLCore8DrawLineEiiiijjb _ZN2GL11_glNormal3sE glIsBuffer _ZN18CTwGraphOpenGLCore7EndDrawEv _ZN14CTwGraphOpenGL4ShutEv _ZN2GL8_glRectdE _ZN2GL13_glTexImage1DE glTexParameterf _ZN14CQuaternionExt11QuatFromDirEPdS0_S0_S0_ddd _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED2Ev _ZN2GL15_glEvalCoord1dvE _ZNSs6appendEPKc glVertexAttrib3sv _ZN2GL14_glLoadMatrixdE memmove@@GLIBC_2.2.5 _ZNK6CTwBar12RotoGetValueEv _ZN6GLCore9_glFinishE _ZNSt6vectorISsSaISsEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPSsS1_EEmRKSs TwDeleteBar _ZN2GL11_glColor3ubE g_TwMasterMgr glColorMask _glActiveTextureARB _ZN6GLCore17_glDeleteTexturesE _ZN2GL14_glTexCoord4dvE _ZN2GL13_glGetDoublevE glEvalMesh1 _ZN6GLCore17_glVertexAttrib2dE _ZN6GLCore13_glTexImage2DE _ZNSt6vectorISsSaISsEED2Ev _ZN2GL11_glColor3dvE g_ErrUnknownAPI TwGetLastError _ZN2GL9_glFinishE _ZN10CTwVarAtom11SetDefaultsEv _ZN2GL12_glDepthFuncE _ZNSt6vectorIiSaIiEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPiS1_EEmRKi _ZN6CTwMgr12SetLastErrorEPKc _ZN14CQuaternionExt10s_ArrowTriE glUniform4f _ZN2GL14_glGetTexGenfvE _ZN2GL12_glGetFloatvE _ZN14CQuaternionExt13MouseButtonCBE16ETwMouseButtonIDbiiiiPvS1_P6CTwBarP11CTwVarGroup glTexSubImage2D _ZN2GL13_glTexCoord3iE TwGetTopBar _ZTVN10__cxxabiv120__si_class_type_infoE glVertexAttrib1sv TwAddSeparator _ZN6CTwMgr18RestoreCDStdStringERKSt6vectorINS_18CCDStdStringRecordESaIS1_EE XFlush g_ErrNthToDo _ZN6GLCore17_glVertexAttrib4dE _ZN6CTwBar22EditInPlaceEraseSelectEv _ZTVSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE TwGetBarByName glArrayElement glGenTextures glGetDoublev glGetPixelMapuiv glVertexAttrib4Nub glNormal3sv glPopName _ZN6GLCore11_glViewportE _ZN2GL10_glColor4sE _ZN6CTwMgr12PixmapCursorEi glIndexdv glVertex3i _ZN2GL10_glTexGeniE glVertex4s _ZNSt6vectorI5CRectSaIS0_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS0_S2_EERKS0_ g_ErrCantLoadOGL glIsProgram _ZN18CTwGraphOpenGLCore7RestoreEv _ZN2GL11_glCallListE _ZN18CTwGraphOpenGLCore8DrawRectEiiiijjjj _ZN14CQuaternionExt6DrawCBEiiPvS0_P6CTwBarP11CTwVarGroup _ZN14CTwGraphOpenGL8DrawRectEiiiij _ZN6GLCore18_glVertexAttrib4ivE _ZN14CQuaternionExt11CreateTypesEv _ZN2GL9_glIndexsE glGetAttachedShaders _ZN6GLCore25_glGetTexLevelParameterivE TwHandleErrors glFeedbackBuffer _ZN6CTwMgr12CMemberProxyC1Ev _ZN2GL10_glLogicOpE glPopAttrib g_KMod glPushAttrib glMap2d glDetachShader glVertex2sv _ZN2GL11_glVertex4fE _ZN2GL16_glTexParameteriE glTexParameteriv g_ErrCantUnloadOGL _ZTS18CTwGraphOpenGLCore glMap2f _ZN2GL8_glFlushE glAttachShader _ZN6GLCore11_glCullFaceE _ZN6GLCore12_glGetStringE _ZN2GL14_glRasterPos4iE _ZN2GL11_glColor3fvE _ZN6GLCore17_glVertexAttrib1dE TwEventKeyGLFWcdecl _ZN6CTwBar10ListValuesERSt6vectorISsSaISsEERS0_IjSaIjEES6_PK8CTexFonti _Jv_RegisterClasses _ZNSt6vectorISsSaISsEED1Ev _ZN6GLCore8_glFlushE _ZNSs4_Rep10_M_destroyERKSaIcE _ZN2GL11_glVertex3fE g_ErrBadSize _ZN2GL14_glTexCoord1ivE _ZN14CQuaternionExt12InitQuat4FCBEPvS0_ _glBlendFuncSeparate g_ErrNotGroup _ZN6CTwBar12CEditInPlaceC2Ev _ZN6CTwMgr16CClientStdStringC1Ev _Z14LoadOpenGLCorev glIsShader _ZN2GL15_glRasterPos3fvE XFreeCursor g_ErrBadValue glIndexMask glDeleteTextures _ZN6CTwBarC1EPKc _ZN2GL11_glNormal3bE _Znwm _ZN2GL10_glEndListE _ZN18CTwGraphOpenGLCore14ChangeViewportEiiiiii glAlphaFunc _ZNSs4_Rep9_S_createEmmRKSaIcE glLightf _ZN2GL10_glRotatedE _ZTS14CTwGraphOpenGL glColor4uiv glRecti glCopyTexSubImage1D glGetLightiv glMaterialiv glGetTexLevelParameterfv _Z16Color32FromARGBiiiii _ZN2GL19_glPushClientAttribE glVertex3iv _glTexImage3D glMapGrid1f _ZN2GL20_glEnableClientStateE glVertexAttrib4f _ZN10CTwVarAtomD1Ev glLightModeli g_FontScaling glTexCoord1sv glVertexAttrib4d glUniformMatrix2fv _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED2Ev _ZN6GLCore17_glSampleCoverageE _ZN2GL10_glDisableE _ZN6CTwVarC2Ev glGetFloatv _ZN6GLCore17_glCopyTexImage2DE glGetMaterialiv _ZN18CTwGraphOpenGLCore10SetScissorEiiii glBindTexture glTranslatef _ZN2GL15_glRasterPos4dvE __cxa_atexit@@GLIBC_2.2.5 _ZN6GLCore12_glPointSizeE glTranslated _ZN2GL10_glIndexubE _ZN6GLCore24_glBlendEquationSeparateE _ZNSt6vectorIP6CTwVarSaIS1_EE9push_backERKS1_ glLinkProgram _ZN6CTwVarD1Ev _ZN2GL14_glTexCoord4ivE _ZN2GL21_glPrioritizeTexturesE sqrt _ZN2GL11_glTexEnvivE _ZN6GLCore19_glVertexAttrib4NbvE _ZN6GLCore16_glCompileShaderE _ZN6GLCore16_glTexParameteriE glNormal3bv _ZN2GL12_glPopAttribE glInterleavedArrays _ZN2GL14_glGetTexImageE glClearDepth _ZNSt6vectorIdSaIdEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPdS1_EERKd g_ErrBadDevice _ZN2GL11_glVertex2sE _ZN6CTwMgrD1Ev _ZN6CTwBar8RotoDrawEv _ZN2GL12_glStencilOpE _ZN2GL7_glFogiE _ZN2GL14_glEvalCoord1dE _ZNSs4_Rep20_S_empty_rep_storageE glGetTexEnvfv _ZN2GL14_glRasterPos2iE __cxa_guard_abort _ZN6GLCore13_glClearDepthE sincosf _ZN6GLCore17_glCopyTexImage1DE _ZN11CTwVarGroup11SetReadOnlyEb _ZNK6CTwVar8IsCustomEv _ZN6CTwMgr13CLibStdStringC2Ev _ZN2GL14_glLightModeliE glPassThrough _ZN2GL17_glTexParameterivE _ZN11CTwVarGroupD0Ev glShadeModel g_ErrIsProcessing _ZN6CTwBar17RotoOnLButtonDownEii glColor3ubv _ZN2GL13_glMatrixModeE _ZN2GL14_glTexCoord3fvE glVertex4sv TwRefreshBar glPolygonMode glCopyPixels g_ErrOutOfRange _ZNSs6assignERKSs glColor3ui glGetMapfv _ZN2GL13_glPushAttribE _ZN14CQuaternionExt18ConvertToAxisAngleEv _ZN2GL14_glRasterPos2fE glRasterPos4fv _ZN6CTwMgr12CStructProxyC2Ev _ZN2GL10_glTexEnvfE _ZN2GL11_glEdgeFlagE _ZNSt8__detail15_List_node_base7_M_hookEPS0_ _ZN2GL13_glPushMatrixE _ZN2GL14_glPolygonModeE glPointParameterf sprintf@@GLIBC_2.2.5 _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE17_M_insert_unique_ESt23_Rb_tree_const_iteratorIS6_ERKS6_ _ZN2GL15_glGetClipPlaneE glGetMapiv _ZN6CTwMgr7CStructC2ERKS0_ _ZN2GL14_glGetTexGenivE _ZN2GL8_glBeginE _ZN2GL25_glGetTexLevelParameterfvE glColor3iv fwrite@@GLIBC_2.2.5 glVertexAttrib4bv _ZN2GL10_glNewListE glUniform3i _ZN2GL12_glVertex4dvE _ZN18CTwGraphOpenGLCore16ResizeTriBuffersEm _ZN2GL14_glTexCoord2fvE glCreateShader _ZNK6CTwBar19RotoGetSteppedValueEv _ZN6GLCore13_glUniform4fvE glVertex2dv _ZN2GL25_glGetTexLevelParameterivE glDisable glColor4iv _ZN6GLCore9_glEnableE _ZN2GL12_glVertex2ivE glGetShaderInfoLog _ZNSt6vectorIN6CTwMgr5CEnumESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN2GL11_glGetErrorE _ZNK6CTwMgr7FindBarEPKc _ZN2GL9_glIndexfE glMapGrid1d g_BreakOnError _ZN6CTwMgr7CStructD2Ev _ZNSt6vectorIjSaIjEED2Ev glVertex4i _ZNK6CTwVar9HasAttribEPKcPb _ZN6CTwBar9SetAttribEiPKc _ITM_registerTMCloneTable glRasterPos2s _ZN6GLCore13_glBlendColorE TwWindowExists glTexImage3D _ZNK6CTwBar9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE glEvalCoord2d _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN6CTwBar23EditInPlaceGetClipboardEPSs glGetTexGenfv glTexEnvi _ZN6GLCore13_glReadBufferE glGetVertexAttribfv _ZN2GL16_glTexParameterfE _ZN6CTwMgr7CStructC1ERKS0_ _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEE7_M_syncEPcmm _Z10IsEnumTypei glRasterPos2i g_ErrInit _ZN11CTwVarGroupD2Ev _ZN6GLCore14_glGetIntegervE _ZN6GLCore18_glVertexAttrib3dvE g_ErrStdString _Z19GetBarVarFromStringPP6CTwBarPP6CTwVarPP11CTwVarGroupPiPKc glTexCoord4fv _ZN14CTwGraphOpenGL10SetScissorEiiii _ZN6CTwBar16EditInPlaceStartEP10CTwVarAtomiii glVertexAttrib4ubv _ZN2GL17_glPixelTransferfE _ZN6GLCore13_glGetDoublevE glRectsv _ZN14CQuaternionExt12s_SphTriProjE glTexCoord4d _ZN6GLCore12_glLineWidthE _ZN2GL20_glGetTexParameterfvE _ZN2GL10_glColor4fE glTexCoord4sv _ZN14CTwGraphOpenGLD2Ev _Z16UnloadOpenGLCorev TwNewBar strdup@@GLIBC_2.2.5 _ZN6CTwMgr7CStruct23s_PassProxyAsClientDataE glDepthRange _ZN6CTwVar9SetAttribEiPKcP6CTwBarP11CTwVarGroupi g_GLUTGetModifiers _ZN14CQuaternionExt12CreateSphereEv _ZN2GL12_glInitNamesE _ZN14CTwGraphOpenGL15RestoreViewportEv _ZN6GLCore18_glVertexAttrib2dvE _ZN6GLCore18_glVertexAttrib4svE glRasterPos3d _ZSt20__throw_length_errorPKc _ZN6GLCore20_glVertexAttrib4NuivE _ZTVN10__cxxabiv117__class_type_infoE _Z12TwGetKeyCodePiS_PKc _ZN8CTexFontC1Ev _ZNSs12_S_constructIPcEES0_T_S1_RKSaIcESt20forward_iterator_tag _ZN2GL15_glLightModelivE glRasterPos2sv _ZN2GL13_glDrawArraysE glVertexAttrib4sv glVertexAttrib4Nubv _ZN6GLCore11_glIsBufferE _ZN2GL14_glEvalCoord2dE _ZNKSs17find_first_not_ofEPKcmm glEdgeFlagv _ZN9CColorExt14CopyVarToExtCBEPKvPvjS2_ glGetIntegerv _ZN14CTwGraphOpenGL8DrawLineEiiiijjb TwEventMouseWheelGLFWcdecl _ZN6GLCore20_glGetProgramInfoLogE _ZN9PerfTimer7GetTimeEv _ZNSolsEi _ZN18CTwGraphOpenGLCore8DrawLineEiiiijb glXGetProcAddressARB _Unwind_Resume@@GCC_3.0 glVertexAttrib3f _ZN6GLCore21_glGetAttachedShadersE _ZN2GL13_glTexCoord4iE _ZSt29_Rb_tree_insert_and_rebalancebPSt18_Rb_tree_node_baseS0_RS_ _ZN2GL10_glColor3fE _ZN2GL13_glTexCoord1iE glVertexAttrib3d _ZN14CTwGraphOpenGL7EndDrawEv _ZN2GL13_glEvalPoint1E _ZN6GLCore13_glUniform3fvE glTexCoord4f _ZN2GL12_glVertex3svE _ZN2GL10_glTexGendE glTexGenfv glMaterialfv _ZN6CTwMgr13CStructMemberD2Ev TwMouseWheel _ZN2GL12_glColor3ubvE glBegin _ZSt17__throw_bad_allocv _ZN2GL9_glRectfvE glRotated glCullFace glRotatef glViewport __cxa_finalize@@GLIBC_2.2.5 glGetPixelMapfv _ZN6GLCore15_glGetUniformfvE _init _ZN6GLCore12_glIsProgramE _ZN14CQuaternionExt13MouseMotionCBEiiiiPvS0_P6CTwBarP11CTwVarGroup _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr7CStructES4_EET0_T_S6_S5_ _ZN2GL12_glNormal3dvE glEvalCoord2f _ZNK6CTwMgr14CheckLastErrorEv glTexCoord1fv TwGetBottomBar _ZN6GLCore13_glUniform2fvE _ZN14CQuaternionExt8s_SphTriE glColor3us glGetQueryObjectiv _ZN2GL12_glMaterialiE glFogiv strstr@@GLIBC_2.2.5 _ZN2GL14_glLoadMatrixfE _ZN2GL13_glTexCoord4fE glCopyTexSubImage2D _ZN6GLCore13_glUniform1ivE _ZN2GL11_glColor4ubE _ZN2GL17_glGetPixelMapuivE glEndList _ZN2GL11_glColor4dvE _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ glVertex4dv _ZN2GL14_glLightModelfE _ZN6CTwBar15BrowseHierarchyEPiiPK6CTwVarii glColor4dv _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _Z14IsCSStringTypei _ZSt16__ostream_insertIcSt11char_traitsIcEERSt13basic_ostreamIT_T0_ES6_PKS3_l _ZN2GL15_glRasterPos2ivE glGetBufferPointerv glVertexAttrib2sv _glGetHandleARB _ZN2GL15_glRasterPos4fvE _ZN2GL11_glColor4usE glPixelTransferf _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6CTwBar12CEditInPlaceD1Ev _ZN2GL17_glDeleteTexturesE glGetBufferSubData glDeleteProgram _ZN2GL10_glTexEnviE usleep@@GLIBC_2.2.5 _ZN6GLCore26_glGetVertexAttribPointervE glPushName _ZN2GL15_glLoadIdentityE stderr@@GLIBC_2.2.5 __cxa_guard_acquire _ZN6GLCore17_glVertexAttrib2fE glDrawElements _ZN2GL14_glDeleteListsE _ZN6CTwMgr12CCDStdString5SetCBEPKvPv _ZN2GL12_glVertex2dvE _ZN6GLCore17_glVertexAttrib3fE _ZSt19__throw_logic_errorPKc glNormal3f _ZN14CTwGraphOpenGL13DrawTrianglesEiPiPjN8ITwGraph4CullE                                                                              �      �      $                              .   ���o       �      �      d$                            8             X&      X&      �                          @             H�      H�      ��                             H   ���o       �]     �]     �                           U   ���o       �j     �j     P                            d             k     k     �V                           n      B       ��     ��     P         
                 x             ��     ��                                   s              �      �     �                            ~             �     �     PQ                            �             `G     `G     	                              �             �G     �G     �_                             �             �     �     <                             �             @�     @�     W                             �             \
	     \
	     2                             �             �")     �"	     (                              �             �")     �"	                                   �             �")     �"	                                   �              #)      #	     H                              �             H')     H'	     �                           �             ())     ()	     �                            �              0)      0	     �
                            �             �:)     �:	     �                                          �N)     hN	     �g                                   0               hN	     -                                                   �N	                                                        �O	     ��         �                 	                      @�	     p�                                                                                                                                                                                                                                             startingcode2/main.cpp                                                                              0000600 0001751 0001751 00000041517 13157110116 013052  0                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     #include<iostream>
#include<fstream>
#include<stdexcept>
#include<sstream>
#include<cmath>
#include <vector>
#include <array>

//*** In this part make sure the include directory contain those head files ***//
//#include "GL/glew.h"
#include "GL/freeglut.h"
#include "GL/glu.h"
#include "AntTweakBar.h"
//********//

#include "vmath.h"

#define PI 3.1415926
#define BUFFER_OFFSET(offset) ((void*)(offset))

typedef enum{PTS, WIREFRAME, SOLID} primitive;
typedef enum{FLAT, SMOOTH} shademode;

//**** animation code ****// You need to further implement the animation class to support the rotating cube
class animation{
public:
	int currenttime_ = 1;
	const int maxtime_ = 1200;
	const float fps = 30.0;
	const int pnum_ = 8;
	vmath::vec3 points_[8] = {
			{0.0, 1.5, -2.0},
			{-3.0, 4.0, 2.3},
			{-6.0, 1.5, -2.5},
			{-4.0, -1.5, 2.8},
			{1.0, -5.0, -4.0},
			{4.0, -1.0, 3.0},
			{7.0, 1.0, -2.0},
			{3.0, 3.0, 3.7}
	};
	void init() {
		currenttime_ = 1;
	}
	void reset() {
		init();
	}
	void update() {}
};

class color{
public:
	vmath::vec4 ambient;
	vmath::vec4 diffuse;
	vmath::vec4 specular;
	float shine;
	color(){
		ambient = vmath::vec4(0,0,0,1);
		diffuse = vmath::vec4(0,0.5,0,1);
		specular = vmath::vec4(0,0,0.5,1);
		shine = 1.0;
	}
};

class model{
public:
	std::vector<vmath::vec3> cube_ = {
			{-1.0f, -1.0f, -1.0f},
			{-1.0f, -1.0f, 1.0f},
			{-1.0f, 1.0f, -1.0f},
			{-1.0f, 1.0f, 1.0f},
			{1.0f, -1.0f, -1.0f},
			{1.0f, -1.0f, 1.0f},
			{1.0f, 1.0f, -1.0f},
			{1.0f, 1.0f, 1.0f}};
	std::vector<std::array<int,3>> cubefaces_ = {
			{0,6,4},{0,2,6},{0,3,2}, {0,1,3}, {2,7,6},{2,3,7},{4,6,7},{4,7,5},{0,4,5},{0,5,1},{1,5,7},{1,7,3}};
	std::vector<vmath::vec3> cnormals_;
	vmath::vec3 cubescale_ = vmath::vec3(0.3f,0.3f,0.3f);
	vmath::vec3 planescale_ = vmath::vec3(6.0f, 6.0f,6.0f);
	vmath::vec3 modelscale_ = vmath::vec3(1.0f, 1.0f, 1.0f);
	vmath::vec3 planetrans_ = vmath::vec3(0.0f, -2.0f, 0.0f);
	vmath::vec3 modelcenter_;
	const int planetrinum_ = 2;
	std::vector<vmath::vec3> plane_ = {
			{1.0f, 0.0f, 1.0f},
			{1.0f, 0.0f,-1.0f},
			{-1.0f, 0.0f,-1.0f},
			{1.0f, 0.0f, 1.0f},
			{-1.0f, 0.0f,-1.0f},
			{-1.0f, 0.0f, 1.0f}};
	std::vector<GLfloat> pnormals_ = {
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f};
	std::vector<vmath::vec3> axis_ = {{-1.0f, 0.0f, 0.0f},
							{1.0f, 0.0f, 0.0f},
							{1.0f, 0.0f, 0.0f},
							{0.75f, 0.25f, 0.0f},
							{1.0f, 0.0f, 0.0f},
							{0.75f, -0.25f, 0.0f},
							{0.0f, -1.0f, 0.0f},
							{0.0f, 1.0f, 0.0f},
							{0.0f, 1.0f, 0.0f},
							{0.25f, 0.75f, 0.0f},
							{0.0f, 1.0f, 0.0f},
							{-0.25f, 0.75f, 0.0f},
							{0.0f, 0.0f, -1.0f},
							{0.0f, 0.0f, 1.0f},
							{0.0f, 0.0f, -1.0f},
							{0.0f, 0.25f, -0.75f},
							{0.0f, 0.0f, -1.0f},
							{0.0f, -0.25f, -0.75f}};
	vmath::vec4 axismat[3] = {{1.0f, 0.0f,0.0f, 0.0f},{0.0f, 1.0f, 0.0f, 0.0f}, {0.0f, 0.0f, 1.0f, 0.0}};
	std::vector<vmath::vec3> vertices_;
	std::vector<std::array<int,3>> triangles_;
	std::vector<vmath::vec3> normals_;
	void load_obj(std::string& filename);
	void init() {
		cnormals_.resize(cubefaces_.size());
		for(unsigned i = 0; i < cubefaces_.size(); ++i) {
			vmath::vec3 ab = cube_[cubefaces_[i][1]] - cube_[cubefaces_[i][0]];
			vmath::vec3 ac = cube_[cubefaces_[i][2]] - cube_[cubefaces_[i][0]];
			cnormals_[i] = normalize(cross(ab, ac));
		}
	}
};

void model::load_obj(std::string& filename) {
	std::ifstream in(filename);
	if(in.fail()) {
		std::cout << filename << std::endl;
		throw std::logic_error("Can't read the mesh filea at BaseMode::ReadObjFile().");
	}
	char buf[256];
	vmath::vec3 pt;
	std::array<int,3> ft;
	vmath::vec3 bmin(INFINITY, INFINITY, INFINITY), bmax(-INFINITY, -INFINITY, -INFINITY);
	while (in.getline(buf, sizeof buf)) {
		std::istringstream line(buf);
		std::string word;
		line >> word;
		if (word == "v"){
			line >> pt[0] >> pt[1] >> pt[2];
			vertices_.push_back(pt);
			for(int k = 0; k < 3; ++k) {
				bmin[k] = std::min(pt[k], bmin[k]);
				bmax[k] = std::max(pt[k], bmax[k]);
			}
		}else if (word == "f"){
			line >> ft[0] >> ft[1] >> ft[2];
			for(unsigned i = 0; i < 3; i++){
				--ft[i];
			}
			triangles_.push_back(ft);
		}
	}
	modelcenter_ = (bmin + bmax);
	modelcenter_ *= 0.5;
	vmath::vec3 diff = bmax - bmin;
	float scale = 1.2 / std::max(std::max(diff[0], diff[1]), diff[2]);
	modelscale_ = vmath::vec3(scale, scale, scale);
	in.close();
	normals_.resize(triangles_.size());
	for(unsigned i = 0; i < triangles_.size(); ++i) {
		vmath::vec3 ab = vertices_[triangles_[i][1]] - vertices_[triangles_[i][0]];
		vmath::vec3 ac = vertices_[triangles_[i][2]] - vertices_[triangles_[i][0]];
		normals_[i] = normalize(cross(ab, ac));
	}
}

class camera{
public:
	vmath::vec3 rot; // This is rotation for camera
	vmath::vec3 trans; // This is translation for camera
	float near_;
	float far_;
	vmath::vec3 lastrot;
	vmath::vec3 lasttrans;
	// transfromation
	vmath::vec3 eye;
	vmath::vec3 u;
	vmath::vec3 v;
	vmath::vec3 w;
	//vmath::mat4 Mc;
	vmath::mat4 M;
	void init() {
		near_ = std::min(0.1, eye[2] + 0.1);
		far_ = std::max(50.0, eye[2] + 40.0);
		reset();
	}
	void reset(){
		rot = vmath::vec3(0,0,0);
		trans = vmath::vec3(0,0,0);
		u = vmath::vec3(1,0,0);
		v = vmath::vec3(0,1,0);
		w = vmath::vec3(0,0,1);
		eye = vmath::vec3(0,0,12);

		lastrot = vmath::vec3(0.0,0.0,0.0);
		lasttrans = vmath::vec3(0.0,0.0,0.0);
		M = vmath::mat4::identity();
	}
	void update() {
		if(rot[0] != lastrot[0] || rot[1] != lastrot[1] || rot[2] != lastrot[2]){
			vmath::mat4 rt = vmath::mat4::identity();
			if(rot[0] != lastrot[0]){
				rt = vmath::rotate(-rot[0] + lastrot[0], vmath::vec3(1,0,0));
			}else if(rot[1] != lastrot[1]){
				rt = vmath::rotate(-rot[1] + lastrot[1], vmath::vec3(0,1,0));
			}else if(rot[2]  != lastrot[2]){
				rt = vmath::rotate(-rot[2] + lastrot[2], vmath::vec3(0,0,1));
			}
			M = rt * M;
			lastrot = rot;
		}
		if(trans[0] != lasttrans[0] || trans[1] != lasttrans[1] || trans[2] != lasttrans[2]){
			vmath::vec3 tt = vmath::vec3(0,0,0);
			if(trans[0] != lasttrans[0]){
				tt = -(trans[0] - lasttrans[0]) * u;
			}else if(trans[1] != lasttrans[1]){
				tt = -(trans[1] - lasttrans[1]) * v;
			}else if(trans[2] != lasttrans[2]){
				tt = -(trans[2] - lasttrans[2]) * w;
			}
			vmath::mat4 mt = vmath::translate(tt);
			M = mt * M;
			//eye = vmath::vec3(M[3][0], M[3][1], M[3][1] + 15);
			lasttrans = trans;
		}
	}
};
class scene{
public:
	bool cw;
	primitive prim;
	shademode shading;

	bool reset_;
	camera camera_;
	animation animate_;

// light para
	bool lightson;
	color light1;
	color light2;

	scene(){
		init();
	}
	void init(){
		cw = false; // using GL_CW
		prim = SOLID;
		shading = FLAT;
		reset_ = false;
		camera_.init();
		animate_.init();
		reset();
	}
	void reset(){
		camera_.reset();

		animate_.reset();

		lightson = true;
		light1.ambient = vmath::vec4(0.5,0.5,0.5,1.0);
		light1.diffuse = vmath::vec4(0.5,0.5,0.5,1.0);
		light1.specular = vmath::vec4(0.1,0.1,0.1,1.0);
		light2.ambient = vmath::vec4(0.5,0.5,0.5,1.0);
		light2.diffuse = vmath::vec4(0.5,0.5,0.5,1.0);
		light2.specular = vmath::vec4(0.1,0.1,0.1,1.0);
	}
};

model gmodel_;
scene g_scene_;

void TW_CALL CopyStdStringToClient(std::string & dst, const std::string & src){
	dst = src;
}

void reshape(int width, int height){
	glViewport(0, 0, width, height);
	TwWindowSize(width, height);
}

void setmaterial(std::string whichone) {
	vmath::vec4 ambient, diffuse, specular;
	GLfloat shine = 0.5;
	if(whichone == "cube") {
		ambient = vmath::vec4(0.4, 0.0, 0.0, 1.0);
		diffuse = vmath::vec4(0.6, 0.0, 0.0, 1.0);
		specular = vmath::vec4(0.0, 0.0, 0.0, 1.0);
	}else if(whichone == "model") {
		ambient = vmath::vec4(0.0, 0.4, 0.0, 1.0);
		diffuse = vmath::vec4(0.0, 0.6, 0.0, 1.0);
		specular = vmath::vec4(0.0, 0.0, 0.0, 1.0);
	}else if(whichone == "axis") {
		ambient = vmath::vec4(0.0, 0.0, 0.5, 1.0);
		diffuse = vmath::vec4(0.0, 0.0, 0.5, 1.0);
		specular = vmath::vec4(0.0, 0.0, 0.0, 1.0);
	}else if(whichone == "plane"){
		ambient = vmath::vec4(0.5, 0.5, 0.5, 1.0);
		diffuse = vmath::vec4(0.5, 0.5, 0.5, 1.0);
		specular = vmath::vec4(0.0, 0.0, 0.0, 1.0);
	}else {
		ambient = vmath::vec4(0.0, 0.5, 0.5, 1.0);
		diffuse = vmath::vec4(0.0, 0.5, 0.5, 1.0);
		specular = vmath::vec4(0.0, 0.0, 0.0, 1.0);
	}
	glMaterialfv(GL_FRONT, GL_AMBIENT, ambient);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
	glMaterialf(GL_FRONT, GL_SHININESS, shine);
}

void lightsInitialize(){
  GLfloat light0_ambient[]  = {0.5, 0.5, 0.5, 1.0 };
  GLfloat light0_diffuse[]  = {0.5, 0.5, 0.5, 1.0 };
  GLfloat light0_specular[] = {0.1, 0.1, 0.1, 1.0 };

  GLfloat light1_ambient[]  = {0.5, 0.5, 0.5, 1.0 };
  GLfloat light1_diffuse[]  = {0.5, 0.5, 0.5, 1.0 };
  GLfloat light1_specular[] = {0.1, 0.1, 0.1, 1.0 };


  glEnable(GL_LIGHTING);

  glEnable(GL_LIGHT0);
  glLightfv(GL_LIGHT0, GL_AMBIENT, light0_ambient);
  glLightfv(GL_LIGHT0, GL_DIFFUSE, light0_diffuse);
  glLightfv(GL_LIGHT0, GL_SPECULAR,light0_specular);

  glEnable(GL_LIGHT1);
  glLightfv(GL_LIGHT1, GL_AMBIENT, light1_ambient);
  glLightfv(GL_LIGHT1, GL_DIFFUSE, light1_diffuse);
  glLightfv(GL_LIGHT1, GL_SPECULAR,light1_specular);
}

void lightsPosition(){
  GLfloat light0_position[] = { 0.0, 10.0, 3.0, 1.0 };
  GLfloat light1_position[] = { -5.0,-10.0,2.0,1.0 };

  glLightfv(GL_LIGHT0, GL_POSITION,light0_position);
  glLightfv(GL_LIGHT1, GL_POSITION,light1_position);
}

void display(){
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	glEnable(GL_NORMALIZE);
	glDepthFunc(GL_LEQUAL);

	switch(g_scene_.prim){
	case PTS:
		glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
		glPointSize(1.0f);
		break;
	case WIREFRAME:
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(1.0f);
		break;
	case SOLID:
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		break;
	default:
		std::cout<< "error prim type!\n";
	}

	if(g_scene_.cw){
		glFrontFace(GL_CW);
	}else{
		glFrontFace(GL_CCW);
	}
	if(g_scene_.reset_){
		g_scene_.reset();
		g_scene_.reset_ = false;
	}

	switch(g_scene_.shading) {
		case FLAT:
			glShadeModel(GL_FLAT);
			break;
		case SMOOTH:
			glShadeModel(GL_SMOOTH);
			break;
		default:
			std::cout << "error shading mode\n";
	}
	if(g_scene_.lightson) {
		lightsInitialize();
		lightsPosition();
	}else{
		glDisable(GL_LIGHTING);
		glColor3f(0.4f, 0.0f, 0.6f);
	}
	//projection matrix
	int width = glutGet(GLUT_WINDOW_WIDTH);
	int height = glutGet(GLUT_WINDOW_HEIGHT);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(60, (double) width / (double) height, g_scene_.camera_.near_, g_scene_.camera_.far_);
// set modelviewmatrix
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	// set viewing matrix
	g_scene_.camera_.update();
	vmath::mat4 Mlookat = vmath::lookat(g_scene_.camera_.eye, g_scene_.camera_.eye - vmath::vec3(0,0,1), vmath::vec3(0,1,0));
	Mlookat = g_scene_.camera_.M * Mlookat;
	GLfloat m[16];
	for(int i = 0; i < 4; ++i){
		for(int j = 0; j < 4; ++j) {
			m[4 * i + j] = Mlookat[i][j];
		}
	}
	glMultMatrixf(m);



	//draw cubes;
	setmaterial("cube");
	for(int i = 0; i < g_scene_.animate_.pnum_; ++i) {
		GLfloat color[4] = {0.1f * i + 0.1f, 0.0f, 1.0f - 0.1f * i, 0.0f};
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, color);
		glPushMatrix();
		glTranslatef(g_scene_.animate_.points_[i][0], g_scene_.animate_.points_[i][1], g_scene_.animate_.points_[i][2]);
		glScalef(gmodel_.cubescale_[0], gmodel_.cubescale_[1], gmodel_.cubescale_[2]);
		glBegin(GL_TRIANGLES);
		for(unsigned i = 0; i < gmodel_.cubefaces_.size(); ++i) {
			glNormal3f(gmodel_.cnormals_[i][0], gmodel_.cnormals_[i][1], gmodel_.cnormals_[i][2]);
			for(int j = 0; j < 3; ++j) {
				glVertex3f(gmodel_.cube_[gmodel_.cubefaces_[i][j]][0], gmodel_.cube_[gmodel_.cubefaces_[i][j]][1], gmodel_.cube_[gmodel_.cubefaces_[i][j]][2]);
			}
		}
		glEnd();
		glPopMatrix();
	}


	g_scene_.animate_.update();
	//draw the curves here





	//draw plane;
	glPushMatrix();
	setmaterial("plane");
	glTranslatef(gmodel_.planetrans_[0], gmodel_.planetrans_[1], gmodel_.planetrans_[2]);
	glScalef(gmodel_.planescale_[0], gmodel_.planescale_[1], gmodel_.planescale_[2]);
	glBegin(GL_TRIANGLES);
	for(int i = 0; i < 2; ++i) {
		glNormal3f(gmodel_.pnormals_[3 * i], gmodel_.pnormals_[3 * i + 1], gmodel_.pnormals_[3 * i + 2]);
		for(int j = 0; j < 3; ++j){
			glVertex3f(gmodel_.plane_[3 * i + j][0], gmodel_.plane_[3 * i + j][1],gmodel_.plane_[3 * i + j][2]);
		}
	}
	glEnd();
	glPopMatrix();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	TwDraw();
	glFlush();
	glutSwapBuffers();
	glutPostRedisplay();
}

void init(){
	gmodel_.init();
	for(int i = 0; i < g_scene_.animate_.pnum_; ++i) {
		gmodel_.planetrans_[1] = std::min(gmodel_.planetrans_[1], g_scene_.animate_.points_[i][1]);
	}
	gmodel_.planetrans_[1] -= 1;
}

void Terminate(void){
	TwTerminate();
}
void reset(){
	g_scene_.reset();
}

void init_gui(){
	TwBar *operbar = TwNewBar("ControlBar");
	TwDefine(" GLOBAL help='CubeAnimation' ");
	TwDefine(" ControlBar position='16 16' size='200 300' color='96 216 224' ");
	TwAddVarRW(operbar,"CW/CCW", TW_TYPE_BOOL8, &g_scene_.cw, NULL);
	TwEnumVal primitivesEV[] = {{PTS,"Point"},{WIREFRAME,"Wireframe"},{SOLID,"Solid"}};
	TwType primtiveType = TwDefineEnum("PrimitiveType",primitivesEV,3);
	TwAddVarRW(operbar,"Primitives", primtiveType, &g_scene_.prim, NULL);
	TwType shadingType;
	shadingType = TwDefineEnum("shadingType", NULL, 0);
	TwAddVarRW(operbar, "Shading", shadingType, &g_scene_.shading, " enum='0{Flat},1{Smooth}' ");

	TwAddSeparator(operbar,"","");
	TwAddVarRW(operbar, "Rot X", TW_TYPE_FLOAT, &g_scene_.camera_.rot[0], "group='Camera Transfromation' min=-360 max=360 step=5");
	TwAddVarRW(operbar, "Rot Y", TW_TYPE_FLOAT, &g_scene_.camera_.rot[1], "group='Camera Transfromation' min=-360 max=360 step=5");
	TwAddVarRW(operbar, "Rot Z", TW_TYPE_FLOAT, &g_scene_.camera_.rot[2], "group='Camera Transfromation' min=-360 max=360 step=5");
	TwAddSeparator(operbar, "", "");
	TwAddVarRW(operbar, "Trans X", TW_TYPE_FLOAT, &g_scene_.camera_.trans[0], "group='Camera Transfromation' min=-20 max=20 step=0.5");
	TwAddVarRW(operbar, "Trans Y", TW_TYPE_FLOAT, &g_scene_.camera_.trans[1], "group='Camera Transfromation' min=-20 max=20 step=0.5");
	TwAddVarRW(operbar, "Trans Z", TW_TYPE_FLOAT, &g_scene_.camera_.trans[2], "group='Camera Transfromation' min=-50 max=50 step=0.5");
	TwAddSeparator(operbar, "","");
	TwAddVarRW(operbar, "Reset", TW_TYPE_BOOL8, &g_scene_.reset_," help='reset to the initial status'");
	TwAddSeparator(operbar, "", "");
	TwAddVarRW(operbar, "LightON", TW_TYPE_BOOL8, &g_scene_.lightson, "");
	TwAddVarRW(operbar, "Ambient1", TW_TYPE_COLOR4F, &g_scene_.light1.ambient, "group=light1");
	TwAddVarRW(operbar, "Diffuse1", TW_TYPE_COLOR4F, &g_scene_.light1.diffuse, "group=light1");
	TwAddVarRW(operbar, "Specular1", TW_TYPE_COLOR4F, &g_scene_.light1.specular, "group=light1");
	TwAddVarRW(operbar, "Ambient2", TW_TYPE_COLOR4F, &g_scene_.light2.ambient, "group=light2");
	TwAddVarRW(operbar, "Diffuse2", TW_TYPE_COLOR4F, &g_scene_.light2.diffuse, "group=light2");
	TwAddVarRW(operbar, "Specular2", TW_TYPE_COLOR4F, &g_scene_.light2.specular, "group=light2");
}

GLvoid Timer(int value) {
	++g_scene_.animate_.currenttime_;
	g_scene_.animate_.currenttime_ %= g_scene_.animate_.maxtime_;
	if( value )
		glutPostRedisplay();
	glutTimerFunc(1200/g_scene_.animate_.fps, Timer, value);
}
int main(int argc, char ** argv){
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(640, 480);
	//glutInitContextVersion(3, 3);
	//glutInitContextProfile(GLUT_CORE_PROFILE);
	glutCreateWindow("Animate");
	glutCreateMenu(NULL);
	//TwInit(TW_OPENGL_CORE, NULL);
	TwInit(TW_OPENGL, NULL);

//	//****The following part is used to test whether your compute support opengl 3.3//
//	glewExperimental = GL_TRUE;
//	if(glewInit()){
//		std::cerr << "Unable to initialize GLEW ...exiting" << std::endl;
//		exit(EXIT_FAILURE);
//	}
//
//	if (glewIsSupported("GL_VERSION_3_3"))
//		std::cout <<"Ready for OpenGL 3.3\n";
//	else {
//		std::cout <<"OpenGL 3.3 not supported\n";
//		exit(1);
//	}
//	//*****//

	// Set GLUT event callbacks
	// - Directly redirect GLUT mouse button events to AntTweakBar
	glutMouseFunc((GLUTmousebuttonfun)TwEventMouseButtonGLUT);
	// - Directly redirect GLUT mouse motion events to AntTweakBar
	glutMotionFunc((GLUTmousemotionfun)TwEventMouseMotionGLUT);
	// - Directly redirect GLUT mouse "passive" motion events to AntTweakBar (same as MouseMotion)
	glutPassiveMotionFunc((GLUTmousemotionfun)TwEventMouseMotionGLUT);
	// - Directly redirect GLUT key events to AntTweakBar
	glutKeyboardFunc((GLUTkeyboardfun)TwEventKeyboardGLUT);

	glutTimerFunc(1200 / g_scene_.animate_.fps, Timer, 0);

	// - Directly redirect GLUT special key events to AntTweakBar
	glutSpecialFunc((GLUTspecialfun)TwEventSpecialGLUT);
	// - Send 'glutGetModifers' function pointer to AntTweakBar;
	//   required because the GLUT key event functions do not report key modifiers states.
	TwGLUTModifiersFunc(glutGetModifiers);

	glutDisplayFunc(display);
	glutReshapeFunc(reshape);

	init();
	init_gui();

	glutMainLoop();
	atexit(Terminate);
	return 0;
}
                                                                                                                                                                                 startingcode2/libAntTweakBar.so.1                                                                   0000711 0001751 0001751 00002537460 13157110116 014772  0                                                                                                    ustar   ni                              ni                                                                                                                                                                                                                     ELF          >    �     @       ��
         @ 8  @                                 �	     �	                   �"	     �")     �")     �+      @�                    H'	     H')     H')     �      �                   �      �      �      $       $              P�td   �     �     �     <      <             Q�td                                                  R�td   �"	     �")     �")     8
 �
 B�( 8X� � ��%@ "    $ 	� A @0(��� B �C�H	�D�!p+@F����4�`1$A "�� �PK�� 8@ ��R� 
�G"��*�a������   �p
m�(�*�
��S0 `" �T ��� 05Љ �  � � ��!P P � E�I��� �K, #@ `4B  (�� $���� �		 P QAЈh @P   
�i@B�H@�  � % �@(    AHDH-����(4� �� l�!r2���F�`��� Q,    ���  �@    �-�� �@2(	TL2C�)@B� 	���I� �a!rH  @@ $ � �H\J( ��[   �B �      !A
0$P8, t
��D  ) ��5	K �$ (0� B*�T-� 
*�T/*H 1��B(��8B5� A	!�  �'�`B�� A�h 0
P P�HA  �@t)B��"*= �H �PV @  � 	�B � ( $ BA P p @ A*��   ��P�� �ID@�Ch    (�   P 
Y"��! �� A`��2� ��@(L 1!� Dh  ȠJD� P( #@�' � � @d@ �;��� ���A�B$�j P BP@��b@�@�!���� J  Ԡ�   @� � �����!  ' D ! �$D"�B�� @$ @@ ���LI�5N��@P� r#A�@� N�@dRD F��� %   �P(	�� ��4�@0� l�    \  ]  _      `  a  b  c  e          f  h  i  j  k      m  o          p  q  u          v  w  z  |  }  �  �  �      �          �      �  �  �      �              �  �  �  �  �                          �                      �      �  �          �      �  �      �      �  �  �  �      �          �  �  �  �      �      �      �      �          �          �  �  �  �  �  �      �  �  �  �          �  �  �  �  �  �  �      �      �  �  �  �      �  �      �  �  �  �                  �  �      �  �  �          �  �          �          �  �  �  �      �  �                         
          
        
                                            !          #  $  '  (          )  .  0  2  3  4              5  6      9          :  ;          <      =  >      @  A          D          G  I  L      M  N  O  P      S          U          X  Y  Z          [  \              ]          ^  `  b                                          c  d  e  g                  i      k      m      n  p      q  r  s  t      v      w          x      y  {  }  ~            �  �  �  �  �  �  �      �  �  �      �      �      �  �  �                          �          �          �      �  �      �  �          �  �  �                      �  �  �      �  �  �          �  �  �  �                      �      �  �  �      �          �          �              �      �  �      �  �  �          �  �  �  �              �  �      �  �  �      �  �      �      �  �  �  �  �  �  �                      �  �      �      �          �          �      �      �              �  �          �  �  �  �      �                                       	  
                                            !  "  #                  $  %  (          *  +  -  .  2  3  6  7  8      9  ;  <          >          ?  C  F  G      H          I  J  K  L  N  O  Q  T  Y  Z  ]  a  c  e  f      i              �}��X���Yr�Ћ�Ab�7�@��?x��(��N�?Sl�,ʦ�]�1C;~C�ïoY�-�d|6�B��g�ɑu��x�-��p��𮓌��	ľ�ag��揗�,3�R��&g�/>V����j3	��UA1���o��H����GM�v����_���у���@�$��Љ+��Ճx�=�MM���T�=�0�p�Kx6����cy�Z�<=�Y"I����-8ي�K$}
v����c��V�CR�?�j��۹�+��3�:�L�����o�F݇��}�b���\������oY7[�4�� gY��#�����jǫ�?Q#�|��2�+Zh S��o}� �IL3N�$<����T�q'�R�+������Xr��`�h*�BE��t�b����MM�������T���c*��S�|��ez>���F�j@5
̍5�AR����Yr��3��c�
ŀ^�׽Q���`���-ʘ?E?N�}��񓍠$IG�S�6�1<!{F��H8G'�&'��'hU.�f�ͥ����7V�=�[�ǖy�yK�q=�2}&36'�@N{u�`(��20+�sV�^�Xp��B����TNq;@�ۙd
�`�"���7�G޷�$+i*��[cVl�飾K�rs�p�#��c�2�����pg�G��r��U;d2��&�{,��r6!��IKHB ]#o������o�v����ϛw�+�X;3����\
��+�В_o�e��l�T�c`I1����I6t�� #t��
�b̨_Ӵqdm�ͫ�U�.yWa9��P��#�e��c���Ճ�Ib�_�rQ!�ϻ��f)t)�k1�1�;��xBY$�����>�1���D��c�Z�d`˿�y��K�`d��|i��u�"/ؒ7'%��r���i+����)M�`D�3�-�� 2�Q��!Yr���S���2�b���~߯g��li*�G�{,ٖr�p
�5,�Э-5�̨�#F���/��@q�F�aFL�e�{1�=��e�4��?/�Xr�@u`�b��>�@o`��l��.'}#��DFo�`%}k�b78ۥ���c?}C�mUqy����`��f�����"/;��ի�Y<�G�-X�1üF���`���=�#c*�3?ov��v]�C�����O����~ߔ�J)Q|�֥w��VA=ߑ`��C�GW��B���'#p�_<Kڙ!�Xi��FXho���F�;E�`��uD];3/
E&��>����3���	�-Yr��|�KG��Uf������5�>98��Wr4����5X�GG�����[�N|}=�tӜ�x1"?�k�X�y�<o�b��-���
k��F�M�c0F���ˢj3:��E��;��P��ɬ-�'ˀ��\�zT� �N߁=i�1�`I{Մؙ��C뒌�$2�+�������ygh&`J])8݀���h�T.pM�1LGoIiط��Y�s���Ji�)�.倏�o5�}D��B���]����u9PG�oxFuC��6+ס�f������Uzq���� Y��,
c�t(�+(�� =��5�����c����J����s��?+�W�����P����w��6��X��E��g�a�te�z]��紐I *ƫ:�n#�lsWr�M*���L㌳�$ꥀ�X`)�gSc5XW��?�)e�� ��ey�G���﬇!����"/�-Y�Yl{����	����(�S�z���6�ʺ�:w��c�  �A�e�1b=+�]�ϻTG����H�V��^��X-���������`��o�(޼φ�cE�,m�/�RJF�y��#��9J��QSQ����~�Ѱ�'�2�c�4��=��Z"q�����%���g��\�-�g���5d�5�� Zcl�MP����K�Yk���*E��wF���=�
߫&��2���<J�q�͜(����Ƹ�mdC}�
���W�pǗ3�e;�Z��ǰ���"�9���%�c�;�`�sG��Ѱ���ܥ/��QEV�
	�̪�n�����	��b+إu;bk��2+n���
�������R�/g���AX��@����p�#`7�O���9��B�i;�k��Ynj��{�`H�U����y9�+w�*��η�Hͪ�S��4n�Eވ�O2��Y�%�Q�g5�}���75/I�̪<�+���z#�
}t�_�Ɓ
C?3��%�O�}�bdp^�����F���
�[�-������+6�)5��Ȓ���4�8��c��}[����x���u�y����q ��3���D��1�@gCE}k�̪�-�J��F�4��=yY�-��l���|1OVJ��Q��IG���p�\�_ρ<L��ͪ�~��3z��=�:�1��x>�=m.��[��M�F��'K«ľ��q�6$��,者'����3G�y���o��?#ZD���|Y�����%J��m��F����A{�u��̪�5/�ˑs|�^�]��"�T��uT��F��#׷�aM�!�2��j g���fu��%m�?�J`��:Eb�e��MG�H��S;��/n�Mtm˗?ڗ�4�.}�:�]����]6}3䩠��l���+��w�g�\v�$F3�p����g��o�˖�4�_�o���-��y'#�y�"���șK7ĕ��Ga�o�ͪ���a�lM<F�+>3@'yz��a�-��ivz	��y1�+GeM����&�]��ޞ��g�����H&RG��t5��P'z-��;����x�`;�qݸ1���F�`��?��]tK��6(�R�}���X��PQ����I�?(}^�a��s�6��A|��Q��Q����ú�v�+���!n��#J���_XYrI>���W^ί�&�=���� ʘW�����a<G���o�^�a߂?sM�+R͢�G8���G�;8`�Y��$���o��27}*'}a��O
r                     �l                     ]X                     ��                     �{                     J�                     �                     �                     zQ                     �n                     Ό                     �h                     g                     tl                     �i                     �l                     "�                     �v                     o�                     |}                     �l                     �                     hs                     a�                     Y�                     Y�                     �                     \                     �u                     {�                     �p                     ��                     f                     0                     eB                     S                     �                     �[                     rZ                     R"                     S�                     Qg                     ˉ                                            �                     6                     5\                     #c                     �Z                     m                     �i                     G                     >h                     �~                     u#                     7~                     �e                     u~                     �                     ��                     na                      �                     �u                     �]                     ��                     �u                     �|                     o                     �i                     Ot                     �a                     �l                     �o                     �l                     �d                     �a                     Lw                     ��                     U�                     �w                     �2                     �d                     �`                     �~                     �4                     z�                     $                     �n                     }f                     �2                     �f                     �b                     ��                     ΋                     ��                     �                     eo                     �                     ��                     j�                     �                     �]                     �m                     *                     �X                     w�                     Ee                     u                     >r                     �h                     ]]                     �{                     �                     k                     V�                     �o                     9                     !                     �[                     /y                     �Z                     �Y                     ��                                          �                     �{                     6R                     �p                     D�                     Jc                     qY                     H�                     �                     Mu                     �j                     �2                     �`                     'x                     �c                     8]                     �v                     �W                     �Z                     �[                     A`                     /Y                     7�                     Wk                     �z                     ��                     S                     �t                      �                     �o                     _                     �o                     �#                     �`                     c_                     #Y                     �g                     b                     {B                     �{                     �-                     �2                     �)                     a"                     
                     �z                     dv                     Ί                     �Y                     �                     <�                     �2                     @                     H�                     �t                     qt                     �r                     !�                     ri                     Gg                     p                     �W                     �V                     ��                     ��                     �u                     og                     �f                     [l                     J�                     �X                     Z                     Bl                     ��                     a                       1                     u�                     YB                     sp                     ]y                     �                     ��                     �N                     �m                     2{                     Rb                     �y                     �s                     4k                     Yq                     �W                     l                     ��                     n                     �_                     �                     ��                     .t                     >s                     �                     |Y                     �,                     �Y                     N�                     !                     zj                     �n                     B                                          0r                     G:                     �                     i                     a                     [}                     �V                     �g                     rz                     z                     :}                     �s                     �c                     �3                     6�                     W$                     �s                     �}                     �                     L�                     ��                     x�                     PY                     �x                     �{                     �                     %l                     �V                     8                       �e                     �                     �v                     �q                     �[                     ,�                     �Q                     f                     �\                     ��                     b                     ]                     \                     �-                     y                     le                     l                     L                     �e                     �                     �                     �R                     �w                      t                     �@                     <V                     �                     ��                     �)                     �                     �\                     [                     zk                     '�                     �<                     �a                     �y                     �a                     ^V                     R   "                   s                     �v                     `                     �|                     }�                     5u                     U                     �y                     �w                     UW                     Z|                     �&                     �                     �                     Oh                     ��                     h�                     �;                     �f                     )                     m,                     �x                     �N                     j                     O    @f     M       �    0O)            ,�    �H)            �    �J)            �    0�     2      H=    У     :       �    �G)            a    X@)            �4  "  ��     v       O    �G)            �r    �D)            }    xK)            �     B)            �X    p>)            V=    �     g       �    ��     5      P  "  ��     �       �    P�)            �;    `=)            Zt    E)            ;y    `F)            h:    PT)     0        ]    �?)            �H    `     6       �    `O)            �[    (?)                �           �  "  P�     �      ��    �K)            \!    ��            3$    p     1
    �2     v       �.  "  �     �      i;    p�     R       $    �>)            &b    �@)            L_    �?)            �    0�     T       5  !  �$)            QV    @U)      @      �     ��     	      V    H=)            �k    �B)            �x    PF)            
      ڊ    �I)            �H    `     �      �/    Ї     �/      ~[    ?)            cl    (C)            �Y    �>)            mU    ��     i      #    Y     v       �    �I)            B<     �            �B  "  ��     I      �C  "  ��     B       �    PO)            Qo    �C)            �z    �F)            �j    �B)            L9    �t     x       =!    ��            Ǎ    �J)            �     ��     	      �H          @       9	    �.     z       �G  "  ��     D       ')  "  P�     A      ^8     R)            e3    �     r      �    @�     �       �}    �G)            �    E)                pK)            �    �L)            $^    �?)            `    �J)            �    0�     T       �Z    �>)            �p    PD)            E    @�     �	      �,  "  P�     E      3T  "  @)     �      �    J)            _    �?)            $<    �            �A    @<)            �T    �     n      �l    PC)            ֛    ��)            h�    �H)            ��    `=     c       ��    �I)            '    �/     �      o>    @�     (       �5    �1     �      j    �B)            �  "  `E            �
       �d    8A)            �]    �?)            �"                 �n    �C)            Z    �=)            
    �F)            (n    �C)            ��    �M)            v�    �I)            0    ��     H      R>    @�     (         !  �&)     `       �    HC)            �     �      m      �]    �?)            �z    �F)            �u    �E)            �    p<            �    �G)            Y    �:)            r  ! 
       �    H)            0P  "  0     (      �    �C)            �q    xD)            42  "  ��            ��    pL)            G    �A)            �>    ��     O      �v    �E)            &  !  �&)     h       q    �M)            OC  "  ��     �      p_     @)            �;    ��     +       ֔     L)            �    �L)            D  "  ��     �       ?�    K)            "     �     �       B\    P?)             �    �I)            �;    `�     �      �    ��     �      �^    �?)            S  "        "      S�    hN)             &_    �?)            �Z    �>)            %}    XG)            �i    �B)            /I  "  �     ;      �#    �	     {      �X    `>)            �\    h?)            �'    P5     R      Y    �M)            �    �D)            �)  "  ��     �      �
  "  �G     �      �5    �-     �      |r    �D)            �9    p�     �      A&    �-     �      8�    �I)            P�    �I)            �    (B)            �:    �<)            �    hI)            �    ��            �s    �D)            �<    Е     2      8�    PH)            Ab    �@)            �0  !  �%)            �=    0�     �       hm    xC)            �U    �=)            yV    �=)            ��    �9     L       �j    �B)            ŏ    0K)            �	    �=)            �`    H@)            �o     D)            �>    �     �       �H  "  ��           �y    �F)            �    �K)            $C  "  ��     �      �    (L)            .7    �8     �       #9    �t     1           HE)            ʕ    HL)            2�    �H)            [     �     W       Jr    �D)            f    ��            Ȁ    (U)            �]    �?)            �    �E)            �n    �C)            �    ��            }    PG)            ��    �L)            =    `<)              "  ��            h�    �H)            �     �           �E    ��           }x    (F)            [z    �F)            t    XK)            =    �     v       �p    HD)            ��    `H)            �    @I)            �W     >)            �    �A     �      �N    �<)            /
     �       �    p-     �      �^    �?)            �h    8B)            f  "  ��            @�    PJ)            �D  "  0�     �      y    @O)            �C  "  0�     M       7    �7     R      �<    p=)            +    �G)            o     E)            �  ! 
�    HJ)            �    @F)            �0  !  �%)            �<    �<)            vn    �C)            �1  "  ��            �    �J)            �E    ��     �      ܈    �I)            +    XC)            cQ    P�     �      ��    (K)            ck    �B)            i    �O)            k/    �q     =      4"    ��     �          �G)            7    ��     5      Nv    �E)            1  !  �%)            V     K)            VQ    `�     �      |8    �Q)            ��    �H)            I  "  �     ;      *a    `@)            `     @)            .    $     �      ;    �|     �       ~
  "  �E     �      Pn    �C)            �    `�)            W
c    �@)            �m    �C)            Ns    �D)            ~    P,     �       ��    p�)            :t    E)            �    H�)            xs    �D)            Te    `A)            2V    �=)            d5    �'           �    �S     ]      p    (D)            �  "   E            >f    �A)            �  "  0�     l       \�    �M)            �    @J)                 ?)            �a    �@)            c  "  E            *4     !            [    �:)            �     @     �      k    �B)            9x    F)            �     X     �       Ԁ    @�)            �    �L)            0L  "   �     �          ��           �;    �T)            t"    �     C       Ǜ    �9     [      �!    ��     '      �_    @)            3g    �A)                ��     �       \    H?)                ��     k      (E  "  ��     E      |    G)            �$    `�     E      "j    �B)            d     A)            �    K)            ��    �M)            =u    XE)            �A     �     !       f=    �<)            |�    �K)            X    8>)            �    ��)             �  "  ��     %       e    PA)            \    �+     �       N;     �     �       ��    �L)            �*    �C     �      �+    �L     �      
      &�    �K)            �    �8            �  "  ��            �|    @G)            Tu    `E)            ?    �D)            \a    p@)            ;=    0�     �       QD  "  ��     �      ��    �J)            4�    �H)            �    0�     B       y	    p/           ;    0}     G      '5  "  P�     ~       ]�    M)            p'     ;)            _?    P�     �      e%    @,     X       �S  "  P"     �      �P  "  `     '      �(    ��     �      U    �F)            V    ��     C	      �>    0=)            #V    �=)            ԁ    hH)            _  "  ��     �       �    3     �      5    Pi     ,      O    �A)              ! 
     �     5       o    �C)            1    �A)            m    `C)            �~    �G)            ��    �H)            �%  "  ��     �      4    �             �  !   #)            Zb    �@)            �u    �E)            �	    8?)            y�    xM)            �1    @Q)     `       "[     ?)            �    pH)            @    ��     	       ��    �>     �       �    �?     �      �          �      �     M)            ��    0J)            ,e    XA)             __gmon_start__ _init _fini _ITM_deregisterTMCloneTable _ITM_registerTMCloneTable __cxa_finalize _Jv_RegisterClasses _Z14ColorRGBToHLSffffPfS_S_ _Z14ColorRGBToHLSiiiiPiS_S_ _Z14ColorHLSToRGBffffPfS_S_ _Z14ColorHLSToRGBiiiiPiS_S_ _Z10ColorBlendjjf _ZN8CTexFontC2Ev _ZN8CTexFontC1Ev _ZN8CTexFontD2Ev _ZdaPv _ZN8CTexFontD1Ev _Z14TwGenerateFontPKhiif g_ErrBadFontHeight g_TwMgr _ZN6CTwMgr12SetLastErrorEPKc _Znwm _Znam memset _ZdlPv _Unwind_Resume __assert_fail __gxx_personality_v0 _Z22TwGenerateDefaultFontsf g_DefaultSmallFont g_DefaultNormalFont g_DefaultLargeFont g_DefaultFixed1Font _Z20TwDeleteDefaultFontsv _ZN8ITwGraphD2Ev _ZTV8ITwGraph _ZN8ITwGraphD1Ev _ZN14CTwGraphOpenGL8DrawLineEiiiijb _ZN14CTwGraphOpenGL8DrawRectEiiiij _ZN14CTwGraphOpenGL9IsDrawingEv _ZN14CTwGraphOpenGL7RestoreEv _ZN2GL17_glDeleteTexturesE _ZN14CTwGraphOpenGL15RestoreViewportEv _ZN2GL11_glViewportE _ZN2GL14_glGetIntegervE _ZN2GL13_glMatrixModeE _ZN2GL14_glLoadMatrixfE _ZN14CTwGraphOpenGLD2Ev _ZN14CTwGraphOpenGLD1Ev _ZN8ITwGraphD0Ev _ZN14CTwGraphOpenGLD0Ev _ZN14CTwGraphOpenGL7EndDrawEv _ZN2GL14_glBindTextureE _glBindVertexArray _glBindBufferARB _glBindProgramARB _glGetHandleARB _glUseProgramObjectARB _glTexImage3D _glBlendEquation _glBlendEquationSeparate _glBlendFuncSeparate _ZN2GL14_glPolygonModeE _ZN2GL10_glTexEnviE _ZN2GL12_glLineWidthE _ZN2GL12_glPopMatrixE _ZN2GL18_glPopClientAttribE _ZN2GL12_glPopAttribE _glActiveTextureARB _ZN2GL9_glEnableE _glClientActiveTextureARB _ZN2GL20_glEnableClientStateE _glEnableVertexAttribArray _ZN14CTwGraphOpenGL8DrawLineEiiiijjb _ZN2GL10_glDisableE _ZN2GL15_glLoadIdentityE _ZN2GL8_glBeginE _ZN2GL11_glColor4ubE _ZN2GL11_glVertex2fE _ZN2GL6_glEndE _ZN14CTwGraphOpenGL8DrawRectEiiiijjjj _ZN14CTwGraphOpenGL13DrawTrianglesEiPiPjN8ITwGraph4CullE _ZN2GL12_glIsEnabledE _ZN2GL11_glCullFaceE _ZN2GL12_glFrontFaceE _ZN14CTwGraphOpenGL9BeginDrawEii _ZN2GL13_glPushAttribE _ZN2GL19_glPushClientAttribE _ZN2GL21_glDisableClientStateE _ZN2GL13_glPushMatrixE _ZN2GL8_glOrthoE _ZN2GL12_glGetFloatvE _ZN2GL12_glBlendFuncE _ZN2GL14_glGetTexEnvivE _glDisableVertexAttribArray _glGetVertexAttribiv _ZN2GL12_glGetStringE strstr _ZN14CTwGraphOpenGL10NewTextObjEv _ZN14CTwGraphOpenGL14ChangeViewportEiiiiii _ZN14CTwGraphOpenGL10SetScissorEiiii _ZN2GL10_glScissorE _ZN14CTwGraphOpenGL4InitEv _Z10LoadOpenGLv _ZN2GL17_glGetProcAddressE g_ErrCantLoadOGL _ZN14CTwGraphOpenGL4ShutEv _Z12UnloadOpenGLv g_ErrCantUnloadOGL _ZN14CTwGraphOpenGL8DrawTextEPviijj _ZN2GL13_glTranslatefE _ZN2GL16_glVertexPointerE _ZN2GL13_glDrawArraysE _ZN2GL18_glTexCoordPointerE _ZN2GL15_glColorPointerE _ZN14CTwGraphOpenGL13DeleteTextObjEPv _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIjSaIjEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPjS1_EERKj memmove _ZNSt6vectorIN14CTwGraphOpenGL4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZSt20__throw_length_errorPKc _ZN14CTwGraphOpenGL9BuildTextEPvPKSsPjS3_iPK8CTexFontii _ZN2GL14_glGenTexturesE _ZN2GL14_glPixelStoreiE _ZN2GL17_glPixelTransferfE _ZN2GL13_glTexImage2DE _ZN2GL16_glTexParameterfE _ZTI8ITwGraph _ZTVN10__cxxabiv117__class_type_infoE _ZTS8ITwGraph _ZTS14CTwGraphOpenGL _ZTI14CTwGraphOpenGL _ZTVN10__cxxabiv120__si_class_type_infoE __cxa_pure_virtual _ZTV14CTwGraphOpenGL g_LargeFontTexID g_NormalFontTexID g_SmallFontTexID _ZN18CTwGraphOpenGLCore8DrawLineEiiiijb _ZN18CTwGraphOpenGLCore8DrawRectEiiiij _ZN18CTwGraphOpenGLCore9IsDrawingEv _ZN18CTwGraphOpenGLCore7RestoreEv _ZN6GLCore17_glDeleteTexturesE _ZN18CTwGraphOpenGLCore14ChangeViewportEiiiiii _ZN18CTwGraphOpenGLCore15RestoreViewportEv _ZN18CTwGraphOpenGLCoreD2Ev _ZN18CTwGraphOpenGLCoreD1Ev _ZN18CTwGraphOpenGLCoreD0Ev _ZN18CTwGraphOpenGLCore9BeginDrawEii _ZN6GLCore14_glGetIntegervE _ZN6GLCore11_glViewportE _ZN6GLCore18_glBindVertexArrayE _ZN6GLCore12_glGetFloatvE _ZN6GLCore12_glLineWidthE _ZN6GLCore12_glIsEnabledE _ZN6GLCore10_glDisableE _ZN6GLCore9_glEnableE _ZN6GLCore12_glBlendFuncE _ZN6GLCore14_glBindTextureE _ZN6GLCore13_glUseProgramE _ZN6GLCore16_glActiveTextureE _ZN18CTwGraphOpenGLCore7EndDrawEv _ZN6GLCore10_glScissorE _ZN18CTwGraphOpenGLCore8DrawLineEiiiijjb _ZN6GLCore13_glBindBufferE _ZN6GLCore16_glBufferSubDataE _ZN6GLCore22_glVertexAttribPointerE _ZN6GLCore26_glEnableVertexAttribArrayE _ZN6GLCore13_glDrawArraysE _ZN18CTwGraphOpenGLCore8DrawRectEiiiijjjj _ZN18CTwGraphOpenGLCore4ShutEv _ZN6GLCore16_glDeleteProgramE _ZN6GLCore15_glDeleteShaderE _ZN6GLCore16_glDeleteBuffersE _ZN6GLCore21_glDeleteVertexArraysE _Z16UnloadOpenGLCorev _ZN18CTwGraphOpenGLCore10NewTextObjEv _ZN18CTwGraphOpenGLCore10SetScissorEiiii _ZN18CTwGraphOpenGLCore13DeleteTextObjEPv _ZN18CTwGraphOpenGLCore16ResizeTriBuffersEm _ZN6GLCore13_glBufferDataE _ZN6GLCore15_glCreateShaderE _ZN6GLCore15_glShaderSourceE _ZN6GLCore16_glCompileShaderE _ZN6GLCore14_glGetShaderivE _ZN6GLCore19_glGetShaderInfoLogE stderr fprintf _ZN6GLCore16_glCreateProgramE _ZN6GLCore15_glAttachShaderE _ZN6GLCore21_glBindAttribLocationE _ZN6GLCore14_glLinkProgramE _ZN6GLCore15_glGetProgramivE _ZN6GLCore20_glGetProgramInfoLogE _ZN6GLCore18_glGenVertexArraysE _ZN6GLCore13_glGenBuffersE _ZN6GLCore21_glGetUniformLocationE _ZN18CTwGraphOpenGLCore4InitEv _Z14LoadOpenGLCorev _ZN18CTwGraphOpenGLCore13DrawTrianglesEiPiPjN8ITwGraph4CullE _ZN6GLCore11_glCullFaceE _ZN6GLCore12_glFrontFaceE _ZN6GLCore12_glUniform2fE _ZN6GLCore27_glDisableVertexAttribArrayE _ZN18CTwGraphOpenGLCore8DrawTextEPviijj _ZN6GLCore12_glUniform4fE _ZN6GLCore12_glUniform1iE _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIN18CTwGraphOpenGLCore4Vec2ESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN18CTwGraphOpenGLCore9BuildTextEPvPKSsPjS3_iPK8CTexFontii _ZN6GLCore14_glGenTexturesE _ZN6GLCore14_glPixelStoreiE _ZN6GLCore13_glTexImage2DE _ZN6GLCore16_glTexParameterfE _ZTS18CTwGraphOpenGLCore _ZTI18CTwGraphOpenGLCore _ZTV18CTwGraphOpenGLCore _ZNK6CTwVar8IsCustomEv _ZNK10CTwVarAtom7IsGroupEv _ZNK10CTwVarAtom10IsReadOnlyEv _ZNK11CTwVarGroup7IsGroupEv _ZN11CTwVarGroup11SetReadOnlyEb _ZNK11CTwVarGroup10IsReadOnlyEv _ZNK10CTwVarAtom18MinMaxStepToDoubleEPdS0_S0_ _ZN11CTwVarGroup12FindShortcutEiiPb _ZNSt6vectorIcSaIcEED2Ev _ZNSt6vectorIcSaIcEED1Ev _ZNK10CTwVarAtom4FindEPKcPP11CTwVarGroupPi strcmp _ZN10CTwVarAtom11SetReadOnlyEb _ZNK6CTwVar9HasAttribEPKcPb strcasecmp _ZNK10CTwVarAtom9HasAttribEPKcPb _ZNK11CTwVarGroup9HasAttribEPKcPb _ZNSt9basic_iosIcSt11char_traitsIcEE5clearESt12_Ios_Iostate _ZNK11CTwVarGroup4FindEPKcPPS_Pi _ZNSs12_M_leak_hardEv _ZNSs6resizeEmc _ZSt20__throw_out_of_rangePKc _ZN6CTwVarD2Ev _ZTV6CTwVar _ZNSs4_Rep20_S_empty_rep_storageE _ZNSs4_Rep10_M_destroyERKSaIcE _ZN6CTwVarD1Ev _ZN6CTwVarD0Ev _ZN10CTwVarAtomD2Ev _ZTV10CTwVarAtom free _ZN6CTwMgr12CCDStdString5GetCBEPvS1_ _ZNSt8__detail15_List_node_base9_M_unhookEv _ZN10CTwVarAtomD1Ev _ZN10CTwVarAtomD0Ev _ZN11CTwVarGroupD2Ev _ZTV11CTwVarGroup _ZN11CTwVarGroupD1Ev _ZN11CTwVarGroupD0Ev _Z16Color32FromARGBiiiii _ZN9PerfTimer7GetTimeEv gettimeofday _Z12IsCustomTypei _ZNK10CTwVarAtom8IsCustomEv _Z14IsCSStringTypei _Z10IsEnumTypei _ZNK10CTwVarAtom13ValueToDoubleEv _ZN10CTwVarAtom15ValueFromDoubleEd _ZN10CTwVarAtom11SetDefaultsEv _ZN10CTwVarAtom9IncrementEi _ZSt18_Rb_tree_incrementPSt18_Rb_tree_node_base fwrite _ZSt18_Rb_tree_decrementPSt18_Rb_tree_node_base _ZN6CTwVarC2Ev _ZN6CTwVarC1Ev _ZN10CTwVarAtomC2Ev _ZN10CTwVarAtomC1Ev _ZN6CTwVar11GetDataSizeE7ETwType _ZNK6CTwBar4FindEPKcPP11CTwVarGroupPi _ZN6CTwBar4FindEPKcPP11CTwVarGroupPi _ZNK6CTwBar9HasAttribEPKcPb _ZN6CTwBar11NotUpToDateEv _ZN6CTwBar9SetAttribEiPKc _Z13TwSetBarStateP6CTwBar8ETwState g_ErrNoValue _ZNSs9_M_mutateEmmm _ZN6CTwMgr9SetAttribEiPKc sscanf strlen _ZNSs6assignEPKcm g_ErrBadValue g_ErrUnknownAttrib _ZNSs6assignERKSs TwSetBottomBar TwSetTopBar TwDeleteBar _ZN6CTwBar12UpdateColorsEv _ZN6CTwBar18ComputeLabelsWidthEPK8CTexFont _ZN6CTwBar18ComputeValuesWidthEPK8CTexFont _ZNSs4_Rep10_M_disposeERKSaIcE _ZN6CTwBar14DrawHierHandleEv _ZN6CTwBar8OpenHierEP11CTwVarGroupP6CTwVar _ZN6CTwBar10LineInHierEP11CTwVarGroupP6CTwVar _Z7DrawArciiiffj sincosf _ZN6CTwBar11CRotoSliderC2Ev _ZN6CTwBar11CRotoSliderC1Ev _ZNK6CTwBar12RotoGetValueEv _ZN6CTwBar12RotoSetValueEd _ZNK6CTwBar10RotoGetMinEv _ZNK6CTwBar10RotoGetMaxEv _ZNK6CTwBar11RotoGetStepEv _ZN6CTwBar8RotoDrawEv sincos _ZNK6CTwBar19RotoGetSteppedValueEv _ZN6CTwBar15RotoOnMouseMoveEii _ZN6CTwMgr9SetCursorEm atan2 acos sqrtf sqrt _ZN6CTwBar17RotoOnLButtonDownEii _ZN6CTwBar15RotoOnLButtonUpEii _ZN6CTwBar17RotoOnMButtonDownEii _ZN6CTwBar15RotoOnMButtonUpEii _ZN6CTwBar12CEditInPlaceC2Ev _ZN6CTwBar12CEditInPlaceC1Ev _ZN6CTwBar12CEditInPlaceD2Ev _ZN6CTwBar12CEditInPlaceD1Ev _ZN6CTwBar21EditInPlaceIsReadOnlyEv _ZN6CTwBar15EditInPlaceDrawEv _ZNSsC1ERKSsmm _ZNSsC1EPKcRKSaIcE _ZN6CTwBar20EditInPlaceAcceptVarEPK10CTwVarAtom _ZN6CTwBar14EditInPlaceEndEb strncpy _ZN6CTwBar16EditInPlaceStartEP10CTwVarAtomiii _ZN6CTwBar22EditInPlaceEraseSelectEv _ZN6CTwBar20EditInPlaceMouseMoveEiib _ZN6CTwBar23EditInPlaceGetClipboardEPSs XFetchBytes memcpy XFree _ZN6CTwBar23EditInPlaceSetClipboardERKSs XSetSelectionOwner XStoreBytes _ZN6CTwBar21EditInPlaceKeyPressedEii _ZNSsC1EmcRKSaIcE _ZNSs6insertEmPKcm _ZNSt6vectorIcSaIcEE6resizeEmc _ZNK10CTwVarAtom13ValueToStringEPSs sprintf _ZNSs7reserveEm _ZNSs6appendEmc _ZNSs6appendERKSs _ZNSsC1ERKSs _ZNSs6appendEPKcm _Z14TwGetKeyStringPSsii _ZNSs6assignEPKc _ZNSt6vectorIP6CTwVarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6CTwVar9SetAttribEiPKcP6CTwBarP11CTwVarGroupi _ZN9CColorExt9SummaryCBEPcmPKvPv TwRemoveVar g_ErrNotGroup _ZN11CTwVarGroup9SetAttribEiPKcP6CTwBarPS_i _ZN14CQuaternionExt9SummaryCBEPcmPKvPv _ZNSt6vectorIdSaIdEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPdS1_EERKd _ZNK6CTwVar9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZSt16__ostream_insertIcSt11char_traitsIcEERSt13basic_ostreamIT_T0_ES6_PKS3_l _ZNK11CTwVarGroup9GetAttribEiP6CTwBarPS_iRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE g_ErrInvalidAttrib _ZStlsISt11char_traitsIcEERSt13basic_ostreamIcT_ES5_PKc _ZNK10CTwVarAtom9GetAttribEiP6CTwBarP11CTwVarGroupiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNKSs13find_first_ofEPKcmm _ZNSo9_M_insertImEERSoT_ _ZNK6CTwBar9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNK6CTwMgr9GetAttribEiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE memcmp _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE16_M_insert_uniqueERKS2_ _ZSt29_Rb_tree_insert_and_rebalancebPSt18_Rb_tree_node_baseS0_RS_ __cxa_begin_catch __cxa_rethrow __cxa_end_catch _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE8_M_eraseEPSt13_Rb_tree_nodeIS7_E _ZN6CTwBarC2EPKc _ZN6CTwBarC1EPKc _ZN6CTwBarD2Ev _ZN6CTwMgr8MaximizeEP6CTwBar _ZN6CTwBarD1Ev _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorISsSaISsEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPSsS1_EERKSs _ZN6CTwBar10ListLabelsERSt6vectorISsSaISsEERS0_IjSaIjEES6_PbPK8CTexFontii _ZN6CTwBar10ListValuesERSt6vectorISsSaISsEERS0_IjSaIjEES6_PK8CTexFonti _ZN6CTwMgr7CStruct14DefaultSummaryEPcmPKvPv _ZNKSs7compareEPKc __cxa_guard_acquire __cxa_guard_release __cxa_atexit _ZNSt8_Rb_treeIPN6CTwMgr12CStructProxyESt4pairIKS2_N6CTwBar13CCustomRecordEESt10_Select1stIS7_ESt4lessIS2_ESaIS7_EE16_M_insert_uniqueERKS7_ _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE8_M_eraseEPSt13_Rb_tree_nodeIS2_E _ZN10CTwVarAtom9SetAttribEiPKcP6CTwBarP11CTwVarGroupi _Z12TwGetKeyCodePiS_PKc strdup _ZSt28_Rb_tree_rebalance_for_erasePSt18_Rb_tree_node_baseRS_ g_ErrUnknownType _ZNSt6vectorIN6CTwBar8CHierTagESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZN6CTwBar15BrowseHierarchyEPiiPK6CTwVarii _ZNSt6vectorISsSaISsEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPSsS1_EEmRKSs _ZNSt6vectorIjSaIjEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPjS1_EEmRKj _ZN6CTwBar6UpdateEv g_BarTimer _ZN6CTwBar4DrawEi _ZN6CTwMgr13UpdateHelpBarEv _ZN6CTwBar11MouseMotionEii _ZN6CTwBar11MouseButtonE16ETwMouseButtonIDbii _ZN6CTwMgr8MinimizeEP6CTwBar TwNewBar TwAddButton _ZN6CTwMgr7SetFontEPK8CTexFontb _ZN6CTwBar10MouseWheelEiiii _ZN6CTwBar7KeyTestEii _ZN6CTwBar4ShowEP6CTwVar _ZN6CTwBar10KeyPressedEii _ZTS6CTwVar _ZTI6CTwVar _ZTS10CTwVarAtom _ZTI10CTwVarAtom _ZTS11CTwVarGroup _ZTI11CTwVarGroup g_ErrNotEnum __pthread_key_create _ZN14CQuaternionExt12MouseLeaveCBEPvS0_P6CTwBar _ZNSt6vectorIfSaIfEED2Ev _ZNSt6vectorIfSaIfEED1Ev _ZN14CQuaternionExt11s_ArrowNormE _ZN14CQuaternionExt10s_ArrowTriE _ZNSt6vectorIiSaIiEED2Ev _ZNSt6vectorIiSaIiEED1Ev _ZN14CQuaternionExt14s_ArrowTriProjE _ZNSt6vectorIjSaIjEED2Ev _ZNSt6vectorIjSaIjEED1Ev _ZN14CQuaternionExt15s_ArrowColLightE glXGetCurrentDisplay XSetErrorHandler XFlush XSync snprintf XAllocNamedColor XCreateBitmapFromData XCreatePixmapCursor XFreePixmap _ZN14CQuaternionExt13MouseButtonCBE16ETwMouseButtonIDbiiiiPvS1_P6CTwBarP11CTwVarGroup _ZN6CTwMgr12CMemberProxy5GetCBEPvS1_ _ZN6CTwMgr7CStruct23s_PassProxyAsClientDataE _ZNSt8__detail15_List_node_base7_M_hookEPS0_ _ZN9CColorExt13InitColor32CBEPvS0_ _ZN9CColorExt13InitColor3FCBEPvS0_ _ZN9CColorExt13InitColor4FCBEPvS0_ _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED2Ev _ZTVSt15basic_stringbufIcSt11char_traitsIcESaIcEE _ZTVSt15basic_streambufIcSt11char_traitsIcEE _ZNSt6localeD1Ev _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED1Ev _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEED0Ev strncat _ZN9CColorExt7RGB2HLSEv _ZN9CColorExt14CopyVarToExtCBEPKvPvjS2_ _ZN9CColorExt7HLS2RGBEv _ZN9CColorExt16CopyVarFromExtCBEPvPKvjS0_ _ZN14CQuaternionExt18ConvertToAxisAngleEv sin _ZN14CQuaternionExt12InitQuat4FCBEPvS0_ _ZN14CQuaternionExt6DrawCBEiiPvS0_P6CTwBarP11CTwVarGroup _ZN14CQuaternionExt13MouseMotionCBEiiiiPvS0_P6CTwBarP11CTwVarGroup _ZN14CQuaternionExt12InitQuat4DCBEPvS0_ _ZN14CQuaternionExt11InitDir3FCBEPvS0_ _ZN14CQuaternionExt11InitDir3DCBEPvS0_ _ZN14CQuaternionExt20ConvertFromAxisAngleEv _ZN14CQuaternionExt16CopyVarFromExtCBEPvPKvjS0_ _ZN14CQuaternionExt9ApplyQuatEPfS0_S0_fffffff _ZN14CQuaternionExt9CopyToVarEv _ZN14CQuaternionExt11QuatFromDirEPdS0_S0_S0_ddd _ZN14CQuaternionExt14CopyVarToExtCBEPKvPvjS2_ _ZN14CQuaternionExt7PermuteEPfS0_S0_fff _ZN14CQuaternionExt8s_SphTriE _ZN14CQuaternionExt8s_SphColE _ZN14CQuaternionExt12s_SphTriProjE _ZN14CQuaternionExt13s_SphColLightE _ZN6CTwMgr16CClientStdStringC2Ev _ZN6CTwMgr16CClientStdStringC1Ev _ZN6CTwMgr16CClientStdString7FromLibEPKc _ZN6CTwMgr16CClientStdString8ToClientEv _ZN6CTwMgr12CCDStdString5SetCBEPKvPv _ZN6CTwMgr12CMemberProxy5SetCBEPKvPv _ZN6CTwMgr13CLibStdStringC2Ev _ZN6CTwMgr13CLibStdStringC1Ev _ZN6CTwMgr13CLibStdString10FromClientERKSs _ZN6CTwMgr13CLibStdString5ToLibEv __cxa_guard_abort TwWindowExists g_Wnds _ZN6CTwMgrC2E11ETwGraphAPIPvi g_InitWndWidth g_InitWndHeight g_InitCopyCDStringToClient g_InitCopyStdStringToClient _ZN6CTwMgrC1E11ETwGraphAPIPvi _ZNK6CTwMgr7FindBarEPKc _ZNK6CTwMgr9HasAttribEPKcPb _ZN6CTwMgr4HideEP6CTwBar _ZN6CTwMgr6UnhideEP6CTwBar _Z13TwGlobalErrorPKc g_ErrorHandler g_BreakOnError TwGetCurrentWindow g_ErrNotInit g_TwMasterMgr TwSetLastError g_ErrIsDrawing usleep TwWindowSize g_ErrBadSize _ZN6CTwMgr12GetLastErrorEv _ZNK6CTwMgr14CheckLastErrorEv _ZN6CTwMgr19SetCurrentDbgParamsEPKci _Z7__TwDbgPKci _Z14TwHandleErrorsPFvPKcEi TwHandleErrors TwGetLastError g_ErrNotFound g_ErrDelHelp g_ErrBadParam TwDeleteAllBars _ZSt17__throw_bad_allocv TwGetTopBar TW_MOUSE_WHEEL TW_MOUSE_MOTION TwGetBottomBar TwGetBarName TwGetBarCount TwGetBarByIndex g_ErrOutOfRange TwGetBarByName TwRefreshBar _ZN6CTwMgr12CStructProxyC2Ev _ZN6CTwMgr12CStructProxyC1Ev _ZN6CTwMgr12CStructProxyD2Ev _ZN6CTwMgr12CStructProxyD1Ev _ZN6CTwMgr18RestoreCDStdStringERKSt6vectorINS_18CCDStdStringRecordESaIS1_EE _ZN6CTwMgr12CMemberProxyC2Ev _ZN6CTwMgr12CMemberProxyC1Ev _ZN6CTwMgr12CMemberProxyD2Ev _ZN6CTwMgr12CMemberProxyD1Ev g_ErrDelStruct _Z10ParseTokenRSsPKcRiS2_bbcc g_TabLength _Z15BarVarHasAttribP6CTwBarP6CTwVarPKcPb _Z15BarVarSetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiPKc TwSetParam _ZNSt8ios_baseC2Ev _ZTVSt9basic_iosIcSt11char_traitsIcEE _ZTTSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt9basic_iosIcSt11char_traitsIcEE4initEPSt15basic_streambufIcS1_E _ZTVSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt6localeC1Ev g_ErrCStrParam _ZNSt19basic_ostringstreamIcSt11char_traitsIcESaIcEED1Ev g_ErrParse _ZNSolsEi _ZNKSt15basic_stringbufIcSt11char_traitsIcESaIcEE3strEv _ZNSt8ios_baseD2Ev _ZNSo9_M_insertIdEERSoT_ _ZN6CTwMgr7CStructD2Ev _ZN6CTwMgr7CStructD1Ev _ZN6CTwMgr13CStructMemberD2Ev _ZN6CTwMgr13CStructMemberD1Ev toupper _ZNSs9push_backEc TwMouseButton TwMouseMotion TW_MOUSE_NA TwMouseWheel TwKeyPressed TwKeyTest _ZN6CTwMgr12PixmapCursorEi _ZN6CTwMgr13CreateCursorsEv XCreateFontCursor _ZN6CTwMgr11FreeCursorsEv XFreeCursor glXGetCurrentDrawable XDefineCursor TwCopyCDStringToClientFunc TwCopyStdStringToClientFunc _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED2Ev _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EED1Ev _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED2Ev _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EED1Ev _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ES3_RKS6_ _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_PKS3_ _ZStplIcSt11char_traitsIcESaIcEESbIT_T0_T1_ERKS6_S8_ _ZNSt6vectorISsSaISsEED2Ev _ZNSt6vectorISsSaISsEED1Ev _ZNSt6vectorIPN6CTwMgr7CCustomESaIS2_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS2_S4_EERKS2_ _ZNSt6vectorIfSaIfEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPfS1_EERKf _ZN14CQuaternionExt12CreateSphereEv _ZNSt6vectorI5CRectSaIS0_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS0_S2_EERKS0_ _ZNK5CRect8SubtractERKS_RSt6vectorIS_SaIS_EE _ZNK5CRect8SubtractERKSt6vectorIS_SaIS_EERS2_ TwDraw _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE8_M_eraseEPSt13_Rb_tree_nodeIS6_E _ZNSt6vectorIdSaIdEE9push_backERKd _Z15BarVarGetAttribP6CTwBarP6CTwVarP11CTwVarGroupiiRSt6vectorIdSaIdEERSt19basic_ostringstreamIcSt11char_traitsIcESaIcEE _ZNSt6vectorIP6CTwBarSaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZNSt6vectorIiSaIiEE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPiS1_EERKi _ZNSt6vectorIbSaIbEE13_M_insert_auxESt13_Bit_iteratorb strchr g_ErrNoBackQuote g_ErrExist _ZNSt6vectorIP6CTwVarSaIS1_EE9push_backERKS1_ _Z19GetBarVarFromStringPP6CTwBarPP6CTwVarPP11CTwVarGroupPiPKc TwDefine g_FontScaling g_ErrStdString TwAddVarRW TwAddVarCB _ZNSs6appendEPKc TwAddVarRO TwAddSeparator _ZN6CTwMgrD2Ev _ZN6CTwMgrD1Ev _ZN6CTwMgr5CEnumD2Ev _ZN6CTwMgr5CEnumD1Ev _ZN6CTwMgr7CStructC2ERKS0_ _ZN6CTwMgr7CStructC1ERKS0_ _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE16_M_insert_uniqueERKS0_ _ZNSt8_Rb_treeI7ETwTypeS0_St9_IdentityIS0_E13StructCompareSaIS0_EE8_M_eraseEPSt13_Rb_tree_nodeIS0_E _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE8_M_eraseEPSt13_Rb_tree_nodeIS4_E TwTerminate _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED2Ev _ZNSt3mapIiP6CTwMgrSt4lessIiESaISt4pairIKiS1_EEED1Ev _ZNSt6vectorIiSaIiEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPiS1_EEmRKi _ZN14CQuaternionExt11CreateArrowEv _ZNSt6vectorIP6CTwVarSaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ TwRemoveAllVars _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EEaSERKS3_ _ZSt18_Rb_tree_incrementPKSt18_Rb_tree_node_base _ZNSt8_Rb_treeIPvSt4pairIKS0_St6vectorIcSaIcEEESt10_Select1stIS6_ESt4lessIS0_ESaIS6_EE17_M_insert_unique_ESt23_Rb_tree_const_iteratorIS6_ERKS6_ _ZNSt6vectorIcSaIcEE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPcS1_EEmRKc TwCopyStdStringToLibrary TwCopyCDStringToLibrary _ZNSt8_Rb_treeIiSt4pairIKiP6CTwMgrESt10_Select1stIS4_ESt4lessIiESaIS4_EE29_M_get_insert_hint_unique_posESt23_Rb_tree_const_iteratorIS4_ERS1_ _ZNSt8_Rb_treeIjSt4pairIKjSsESt10_Select1stIS2_ESt4lessIjESaIS2_EE7_M_copyEPKSt13_Rb_tree_nodeIS2_EPSA_ _ZN6CTwMgr5CEnumC2ERKS0_ _ZN6CTwMgr5CEnumC1ERKS0_ _ZNSs12_S_constructIPcEES0_T_S1_RKSaIcESt20forward_iterator_tag _ZNSs4_Rep9_S_createEmmRKSaIcE _ZSt19__throw_logic_errorPKc TwGetParam g_ErrHasNoValue g_ErrBadType _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr18CCDStdStringRecordES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ _ZN6CTwMgr17UnrollCDStdStringERSt6vectorINS_18CCDStdStringRecordESaIS1_EE7ETwTypePv _ZNSt6vectorIN6CTwMgr18CCDStdStringRecordESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr5CEnumES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr5CEnumESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ TwDefineEnum TwDefineEnumFromString _ZTTSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZTVSt18basic_stringstreamIcSt11char_traitsIcESaIcEE _ZNSsC1EPKcmRKSaIcE _ZNSt15basic_stringbufIcSt11char_traitsIcESaIcEE7_M_syncEPcmm _ZSt7getlineIcSt11char_traitsIcESaIcEERSt13basic_istreamIT_T0_ES7_RSbIS4_S5_T1_ES4_ _ZNKSs17find_first_not_ofEPKcmm _ZNKSs16find_last_not_ofEPKcmm _ZNSt18basic_stringstreamIcSt11char_traitsIcESaIcEED1Ev _ZNSdD2Ev _ZNSt22__uninitialized_fill_nILb0EE15__uninit_fill_nIPN6CTwMgr13CStructMemberEmS3_EEvT_T0_RKT1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr13CStructMemberES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr13CStructMemberESaIS1_EE14_M_fill_insertEN9__gnu_cxx17__normal_iteratorIPS1_S3_EEmRKS1_ _ZNSt20__uninitialized_copyILb0EE13__uninit_copyIPN6CTwMgr7CStructES4_EET0_T_S6_S5_ _ZNSt6vectorIN6CTwMgr7CStructESaIS1_EE13_M_insert_auxEN9__gnu_cxx17__normal_iteratorIPS1_S3_EERKS1_ TwDefineStruct g_ErrOffset _Z17TwDefineStructExtPKcPK15CTwStructMemberjmmPFvPvS4_EPFvS4_PKvjS4_EPFvS8_S4_jS4_EPFvPcmS8_S4_ES4_S0_ _ZN9CColorExt11CreateTypesEv _ZN14CQuaternionExt11CreateTypesEv _ZN14CQuaternionExt12s_CustomTypeE TwInit g_ErrInit g_ErrUnknownAPI TwSetCurrentWindow g_ErrIsProcessing g_ErrNthToDo g_ErrBadDevice g_ErrShut glXGetProcAddressARB g_OGLFuncRec glViewport glVertexPointer _ZN2GL12_glVertex4svE glVertex4sv _ZN2GL11_glVertex4sE glVertex4s _ZN2GL12_glVertex4ivE glVertex4iv _ZN2GL11_glVertex4iE glVertex4i _ZN2GL12_glVertex4fvE glVertex4fv _ZN2GL11_glVertex4fE glVertex4f _ZN2GL12_glVertex4dvE glVertex4dv _ZN2GL11_glVertex4dE glVertex4d _ZN2GL12_glVertex3svE glVertex3sv _ZN2GL11_glVertex3sE glVertex3s _ZN2GL12_glVertex3ivE glVertex3iv _ZN2GL11_glVertex3iE glVertex3i _ZN2GL12_glVertex3fvE glVertex3fv _ZN2GL11_glVertex3fE glVertex3f _ZN2GL12_glVertex3dvE glVertex3dv _ZN2GL11_glVertex3dE glVertex3d _ZN2GL12_glVertex2svE glVertex2sv _ZN2GL11_glVertex2sE glVertex2s _ZN2GL12_glVertex2ivE glVertex2iv _ZN2GL11_glVertex2iE glVertex2i _ZN2GL12_glVertex2fvE glVertex2fv glVertex2f _ZN2GL12_glVertex2dvE glVertex2dv _ZN2GL11_glVertex2dE glVertex2d glTranslatef _ZN2GL13_glTranslatedE glTranslated _ZN2GL16_glTexSubImage2DE glTexSubImage2D _ZN2GL16_glTexSubImage1DE glTexSubImage1D _ZN2GL17_glTexParameterivE glTexParameteriv _ZN2GL16_glTexParameteriE glTexParameteri _ZN2GL17_glTexParameterfvE glTexParameterfv glTexParameterf glTexImage2D _ZN2GL13_glTexImage1DE glTexImage1D _ZN2GL11_glTexGenivE glTexGeniv _ZN2GL10_glTexGeniE glTexGeni _ZN2GL11_glTexGenfvE glTexGenfv _ZN2GL10_glTexGenfE glTexGenf _ZN2GL11_glTexGendvE glTexGendv _ZN2GL10_glTexGendE glTexGend _ZN2GL11_glTexEnvivE glTexEnviv glTexEnvi _ZN2GL11_glTexEnvfvE glTexEnvfv _ZN2GL10_glTexEnvfE glTexEnvf glTexCoordPointer _ZN2GL14_glTexCoord4svE glTexCoord4sv _ZN2GL13_glTexCoord4sE glTexCoord4s _ZN2GL14_glTexCoord4ivE glTexCoord4iv _ZN2GL13_glTexCoord4iE glTexCoord4i _ZN2GL14_glTexCoord4fvE glTexCoord4fv _ZN2GL13_glTexCoord4fE glTexCoord4f _ZN2GL14_glTexCoord4dvE glTexCoord4dv _ZN2GL13_glTexCoord4dE glTexCoord4d _ZN2GL14_glTexCoord3svE glTexCoord3sv _ZN2GL13_glTexCoord3sE glTexCoord3s _ZN2GL14_glTexCoord3ivE glTexCoord3iv _ZN2GL13_glTexCoord3iE glTexCoord3i _ZN2GL14_glTexCoord3fvE glTexCoord3fv _ZN2GL13_glTexCoord3fE glTexCoord3f _ZN2GL14_glTexCoord3dvE glTexCoord3dv _ZN2GL13_glTexCoord3dE glTexCoord3d _ZN2GL14_glTexCoord2svE glTexCoord2sv _ZN2GL13_glTexCoord2sE glTexCoord2s _ZN2GL14_glTexCoord2ivE glTexCoord2iv _ZN2GL13_glTexCoord2iE glTexCoord2i _ZN2GL14_glTexCoord2fvE glTexCoord2fv _ZN2GL13_glTexCoord2fE glTexCoord2f _ZN2GL14_glTexCoord2dvE glTexCoord2dv _ZN2GL13_glTexCoord2dE glTexCoord2d _ZN2GL14_glTexCoord1svE glTexCoord1sv _ZN2GL13_glTexCoord1sE glTexCoord1s _ZN2GL14_glTexCoord1ivE glTexCoord1iv _ZN2GL13_glTexCoord1iE glTexCoord1i _ZN2GL14_glTexCoord1fvE glTexCoord1fv _ZN2GL13_glTexCoord1fE glTexCoord1f _ZN2GL14_glTexCoord1dvE glTexCoord1dv _ZN2GL13_glTexCoord1dE glTexCoord1d _ZN2GL12_glStencilOpE glStencilOp _ZN2GL14_glStencilMaskE glStencilMask _ZN2GL14_glStencilFuncE glStencilFunc _ZN2GL13_glShadeModelE glShadeModel _ZN2GL15_glSelectBufferE glSelectBuffer glScissor _ZN2GL9_glScalefE glScalef _ZN2GL9_glScaledE glScaled _ZN2GL10_glRotatefE glRotatef _ZN2GL10_glRotatedE glRotated _ZN2GL13_glRenderModeE glRenderMode _ZN2GL9_glRectsvE glRectsv _ZN2GL8_glRectsE glRects _ZN2GL9_glRectivE glRectiv _ZN2GL8_glRectiE glRecti _ZN2GL9_glRectfvE glRectfv _ZN2GL8_glRectfE glRectf _ZN2GL9_glRectdvE glRectdv _ZN2GL8_glRectdE glRectd _ZN2GL13_glReadPixelsE glReadPixels _ZN2GL13_glReadBufferE glReadBuffer _ZN2GL15_glRasterPos4svE glRasterPos4sv _ZN2GL14_glRasterPos4sE glRasterPos4s _ZN2GL15_glRasterPos4ivE glRasterPos4iv _ZN2GL14_glRasterPos4iE glRasterPos4i _ZN2GL15_glRasterPos4fvE glRasterPos4fv _ZN2GL14_glRasterPos4fE glRasterPos4f _ZN2GL15_glRasterPos4dvE glRasterPos4dv _ZN2GL14_glRasterPos4dE glRasterPos4d _ZN2GL15_glRasterPos3svE glRasterPos3sv _ZN2GL14_glRasterPos3sE glRasterPos3s _ZN2GL15_glRasterPos3ivE glRasterPos3iv _ZN2GL14_glRasterPos3iE glRasterPos3i _ZN2GL15_glRasterPos3fvE glRasterPos3fv _ZN2GL14_glRasterPos3fE glRasterPos3f _ZN2GL15_glRasterPos3dvE glRasterPos3dv _ZN2GL14_glRasterPos3dE glRasterPos3d _ZN2GL15_glRasterPos2svE glRasterPos2sv _ZN2GL14_glRasterPos2sE glRasterPos2s _ZN2GL15_glRasterPos2ivE glRasterPos2iv _ZN2GL14_glRasterPos2iE glRasterPos2i _ZN2GL15_glRasterPos2fvE glRasterPos2fv _ZN2GL14_glRasterPos2fE glRasterPos2f _ZN2GL15_glRasterPos2dvE glRasterPos2dv _ZN2GL14_glRasterPos2dE glRasterPos2d _ZN2GL11_glPushNameE glPushName glPushMatrix glPushClientAttrib glPushAttrib _ZN2GL21_glPrioritizeTexturesE glPrioritizeTextures _ZN2GL10_glPopNameE glPopName glPopMatrix glPopClientAttrib glPopAttrib _ZN2GL17_glPolygonStippleE glPolygonStipple _ZN2GL16_glPolygonOffsetE glPolygonOffset glPolygonMode _ZN2GL12_glPointSizeE glPointSize _ZN2GL12_glPixelZoomE glPixelZoom _ZN2GL17_glPixelTransferiE glPixelTransferi glPixelTransferf glPixelStorei _ZN2GL14_glPixelStorefE glPixelStoref _ZN2GL14_glPixelMapusvE glPixelMapusv _ZN2GL14_glPixelMapuivE glPixelMapuiv _ZN2GL13_glPixelMapfvE glPixelMapfv _ZN2GL14_glPassThroughE glPassThrough glOrtho _ZN2GL16_glNormalPointerE glNormalPointer _ZN2GL12_glNormal3svE glNormal3sv _ZN2GL11_glNormal3sE glNormal3s _ZN2GL12_glNormal3ivE glNormal3iv _ZN2GL11_glNormal3iE glNormal3i _ZN2GL12_glNormal3fvE glNormal3fv _ZN2GL11_glNormal3fE glNormal3f _ZN2GL12_glNormal3dvE glNormal3dv _ZN2GL11_glNormal3dE glNormal3d _ZN2GL12_glNormal3bvE glNormal3bv _ZN2GL11_glNormal3bE glNormal3b _ZN2GL10_glNewListE glNewList _ZN2GL14_glMultMatrixfE glMultMatrixf _ZN2GL14_glMultMatrixdE glMultMatrixd glMatrixMode _ZN2GL13_glMaterialivE glMaterialiv _ZN2GL12_glMaterialiE glMateriali _ZN2GL13_glMaterialfvE glMaterialfv _ZN2GL12_glMaterialfE glMaterialf _ZN2GL12_glMapGrid2fE glMapGrid2f _ZN2GL12_glMapGrid2dE glMapGrid2d _ZN2GL12_glMapGrid1fE glMapGrid1f _ZN2GL12_glMapGrid1dE glMapGrid1d _ZN2GL8_glMap2fE glMap2f _ZN2GL8_glMap2dE glMap2d _ZN2GL8_glMap1fE glMap1f _ZN2GL8_glMap1dE glMap1d _ZN2GL10_glLogicOpE glLogicOp _ZN2GL11_glLoadNameE glLoadName glLoadMatrixf _ZN2GL14_glLoadMatrixdE glLoadMatrixd glLoadIdentity _ZN2GL11_glListBaseE glListBase glLineWidth _ZN2GL14_glLineStippleE glLineStipple _ZN2GL10_glLightivE glLightiv _ZN2GL9_glLightiE glLighti _ZN2GL10_glLightfvE glLightfv _ZN2GL9_glLightfE glLightf _ZN2GL15_glLightModelivE glLightModeliv _ZN2GL14_glLightModeliE glLightModeli _ZN2GL15_glLightModelfvE glLightModelfv _ZN2GL14_glLightModelfE glLightModelf _ZN2GL12_glIsTextureE glIsTexture _ZN2GL9_glIsListE glIsList glIsEnabled _ZN2GL20_glInterleavedArraysE glInterleavedArrays _ZN2GL12_glInitNamesE glInitNames _ZN2GL11_glIndexubvE glIndexubv _ZN2GL10_glIndexubE glIndexub _ZN2GL10_glIndexsvE glIndexsv _ZN2GL9_glIndexsE glIndexs _ZN2GL10_glIndexivE glIndexiv _ZN2GL9_glIndexiE glIndexi _ZN2GL10_glIndexfvE glIndexfv _ZN2GL9_glIndexfE glIndexf _ZN2GL10_glIndexdvE glIndexdv _ZN2GL9_glIndexdE glIndexd _ZN2GL15_glIndexPointerE glIndexPointer _ZN2GL12_glIndexMaskE glIndexMask _ZN2GL7_glHintE glHint _ZN2GL20_glGetTexParameterivE glGetTexParameteriv _ZN2GL20_glGetTexParameterfvE glGetTexParameterfv _ZN2GL25_glGetTexLevelParameterivE glGetTexLevelParameteriv _ZN2GL25_glGetTexLevelParameterfvE glGetTexLevelParameterfv _ZN2GL14_glGetTexImageE glGetTexImage _ZN2GL14_glGetTexGenivE glGetTexGeniv _ZN2GL14_glGetTexGenfvE glGetTexGenfv _ZN2GL14_glGetTexGendvE glGetTexGendv glGetTexEnviv _ZN2GL14_glGetTexEnvfvE glGetTexEnvfv glGetString _ZN2GL20_glGetPolygonStippleE glGetPolygonStipple _ZN2GL14_glGetPointervE glGetPointerv _ZN2GL17_glGetPixelMapusvE glGetPixelMapusv _ZN2GL17_glGetPixelMapuivE glGetPixelMapuiv _ZN2GL16_glGetPixelMapfvE glGetPixelMapfv _ZN2GL16_glGetMaterialivE glGetMaterialiv _ZN2GL16_glGetMaterialfvE glGetMaterialfv _ZN2GL11_glGetMapivE glGetMapiv _ZN2GL11_glGetMapfvE glGetMapfv _ZN2GL11_glGetMapdvE glGetMapdv _ZN2GL13_glGetLightivE glGetLightiv _ZN2GL13_glGetLightfvE glGetLightfv glGetIntegerv glGetFloatv _ZN2GL11_glGetErrorE glGetError _ZN2GL13_glGetDoublevE glGetDoublev _ZN2GL15_glGetClipPlaneE glGetClipPlane _ZN2GL14_glGetBooleanvE glGetBooleanv glGenTextures _ZN2GL11_glGenListsE glGenLists _ZN2GL10_glFrustumE glFrustum glFrontFace _ZN2GL8_glFogivE glFogiv _ZN2GL7_glFogiE glFogi _ZN2GL8_glFogfvE glFogfv _ZN2GL7_glFogfE glFogf _ZN2GL8_glFlushE glFlush _ZN2GL9_glFinishE glFinish _ZN2GL17_glFeedbackBufferE glFeedbackBuffer _ZN2GL13_glEvalPoint2E glEvalPoint2 _ZN2GL13_glEvalPoint1E glEvalPoint1 _ZN2GL12_glEvalMesh2E glEvalMesh2 _ZN2GL12_glEvalMesh1E glEvalMesh1 _ZN2GL15_glEvalCoord2fvE glEvalCoord2fv _ZN2GL14_glEvalCoord2fE glEvalCoord2f _ZN2GL15_glEvalCoord2dvE glEvalCoord2dv _ZN2GL14_glEvalCoord2dE glEvalCoord2d _ZN2GL15_glEvalCoord1fvE glEvalCoord1fv _ZN2GL14_glEvalCoord1fE glEvalCoord1f _ZN2GL15_glEvalCoord1dvE glEvalCoord1dv _ZN2GL14_glEvalCoord1dE glEvalCoord1d _ZN2GL10_glEndListE glEndList glEnd glEnableClientState glEnable _ZN2GL12_glEdgeFlagvE glEdgeFlagv _ZN2GL18_glEdgeFlagPointerE glEdgeFlagPointer _ZN2GL11_glEdgeFlagE glEdgeFlag _ZN2GL13_glDrawPixelsE glDrawPixels _ZN2GL15_glDrawElementsE glDrawElements _ZN2GL13_glDrawBufferE glDrawBuffer glDrawArrays glDisableClientState glDisable _ZN2GL13_glDepthRangeE glDepthRange _ZN2GL12_glDepthMaskE glDepthMask _ZN2GL12_glDepthFuncE glDepthFunc glDeleteTextures _ZN2GL14_glDeleteListsE glDeleteLists glCullFace _ZN2GL20_glCopyTexSubImage2DE glCopyTexSubImage2D _ZN2GL20_glCopyTexSubImage1DE glCopyTexSubImage1D _ZN2GL17_glCopyTexImage2DE glCopyTexImage2D _ZN2GL17_glCopyTexImage1DE glCopyTexImage1D _ZN2GL13_glCopyPixelsE glCopyPixels glColorPointer _ZN2GL16_glColorMaterialE glColorMaterial _ZN2GL12_glColorMaskE glColorMask _ZN2GL12_glColor4usvE glColor4usv _ZN2GL11_glColor4usE glColor4us _ZN2GL12_glColor4uivE glColor4uiv _ZN2GL11_glColor4uiE glColor4ui _ZN2GL12_glColor4ubvE glColor4ubv glColor4ub _ZN2GL11_glColor4svE glColor4sv _ZN2GL10_glColor4sE glColor4s _ZN2GL11_glColor4ivE glColor4iv _ZN2GL10_glColor4iE glColor4i _ZN2GL11_glColor4fvE glColor4fv _ZN2GL10_glColor4fE glColor4f _ZN2GL11_glColor4dvE glColor4dv _ZN2GL10_glColor4dE glColor4d _ZN2GL11_glColor4bvE glColor4bv _ZN2GL10_glColor4bE glColor4b _ZN2GL12_glColor3usvE glColor3usv _ZN2GL11_glColor3usE glColor3us _ZN2GL12_glColor3uivE glColor3uiv _ZN2GL11_glColor3uiE glColor3ui _ZN2GL12_glColor3ubvE glColor3ubv _ZN2GL11_glColor3ubE glColor3ub _ZN2GL11_glColor3svE glColor3sv _ZN2GL10_glColor3sE glColor3s _ZN2GL11_glColor3ivE glColor3iv _ZN2GL10_glColor3iE glColor3i _ZN2GL11_glColor3fvE glColor3fv _ZN2GL10_glColor3fE glColor3f _ZN2GL11_glColor3dvE glColor3dv _ZN2GL10_glColor3dE glColor3d _ZN2GL11_glColor3bvE glColor3bv _ZN2GL10_glColor3bE glColor3b _ZN2GL12_glClipPlaneE glClipPlane _ZN2GL15_glClearStencilE glClearStencil _ZN2GL13_glClearIndexE glClearIndex _ZN2GL13_glClearDepthE glClearDepth _ZN2GL13_glClearColorE glClearColor _ZN2GL13_glClearAccumE glClearAccum _ZN2GL8_glClearE glClear _ZN2GL12_glCallListsE glCallLists _ZN2GL11_glCallListE glCallList glBlendFunc _ZN2GL9_glBitmapE glBitmap glBindTexture glBegin _ZN2GL15_glArrayElementE glArrayElement _ZN2GL22_glAreTexturesResidentE glAreTexturesResident _ZN2GL12_glAlphaFuncE glAlphaFunc _ZN2GL8_glAccumE glAccum g_NbOGLFunc _ZN6GLCore17_glGetProcAddressE _ZN6GLCore16_glIsVertexArrayE g_OGLCoreFuncRec glVertexAttribPointer _ZN6GLCore19_glVertexAttrib4usvE glVertexAttrib4usv _ZN6GLCore19_glVertexAttrib4uivE glVertexAttrib4uiv _ZN6GLCore19_glVertexAttrib4ubvE glVertexAttrib4ubv _ZN6GLCore18_glVertexAttrib4svE glVertexAttrib4sv _ZN6GLCore17_glVertexAttrib4sE glVertexAttrib4s _ZN6GLCore18_glVertexAttrib4ivE glVertexAttrib4iv _ZN6GLCore18_glVertexAttrib4fvE glVertexAttrib4fv _ZN6GLCore17_glVertexAttrib4fE glVertexAttrib4f _ZN6GLCore18_glVertexAttrib4dvE glVertexAttrib4dv _ZN6GLCore17_glVertexAttrib4dE glVertexAttrib4d _ZN6GLCore18_glVertexAttrib4bvE glVertexAttrib4bv _ZN6GLCore20_glVertexAttrib4NusvE glVertexAttrib4Nusv _ZN6GLCore20_glVertexAttrib4NuivE glVertexAttrib4Nuiv _ZN6GLCore20_glVertexAttrib4NubvE glVertexAttrib4Nubv _ZN6GLCore19_glVertexAttrib4NubE glVertexAttrib4Nub _ZN6GLCore19_glVertexAttrib4NsvE glVertexAttrib4Nsv _ZN6GLCore19_glVertexAttrib4NivE glVertexAttrib4Niv _ZN6GLCore19_glVertexAttrib4NbvE glVertexAttrib4Nbv _ZN6GLCore18_glVertexAttrib3svE glVertexAttrib3sv _ZN6GLCore17_glVertexAttrib3sE glVertexAttrib3s _ZN6GLCore18_glVertexAttrib3fvE glVertexAttrib3fv _ZN6GLCore17_glVertexAttrib3fE glVertexAttrib3f _ZN6GLCore18_glVertexAttrib3dvE glVertexAttrib3dv _ZN6GLCore17_glVertexAttrib3dE glVertexAttrib3d _ZN6GLCore18_glVertexAttrib2svE glVertexAttrib2sv _ZN6GLCore17_glVertexAttrib2sE glVertexAttrib2s _ZN6GLCore18_glVertexAttrib2fvE glVertexAttrib2fv _ZN6GLCore17_glVertexAttrib2fE glVertexAttrib2f _ZN6GLCore18_glVertexAttrib2dvE glVertexAttrib2dv _ZN6GLCore17_glVertexAttrib2dE glVertexAttrib2d _ZN6GLCore18_glVertexAttrib1svE glVertexAttrib1sv _ZN6GLCore17_glVertexAttrib1sE glVertexAttrib1s _ZN6GLCore18_glVertexAttrib1fvE glVertexAttrib1fv _ZN6GLCore17_glVertexAttrib1fE glVertexAttrib1f _ZN6GLCore18_glVertexAttrib1dvE glVertexAttrib1dv _ZN6GLCore17_glVertexAttrib1dE glVertexAttrib1d _ZN6GLCore18_glValidateProgramE glValidateProgram _ZN6GLCore19_glUniformMatrix4fvE glUniformMatrix4fv _ZN6GLCore19_glUniformMatrix3fvE glUniformMatrix3fv _ZN6GLCore19_glUniformMatrix2fvE glUniformMatrix2fv _ZN6GLCore13_glUniform4ivE glUniform4iv _ZN6GLCore13_glUniform3ivE glUniform3iv _ZN6GLCore13_glUniform2ivE glUniform2iv _ZN6GLCore13_glUniform1ivE glUniform1iv _ZN6GLCore13_glUniform4fvE glUniform4fv _ZN6GLCore13_glUniform3fvE glUniform3fv _ZN6GLCore13_glUniform2fvE glUniform2fv _ZN6GLCore13_glUniform1fvE glUniform1fv _ZN6GLCore12_glUniform4iE glUniform4i _ZN6GLCore12_glUniform3iE glUniform3i _ZN6GLCore12_glUniform2iE glUniform2i glUniform1i glUniform4f _ZN6GLCore12_glUniform3fE glUniform3f glUniform2f _ZN6GLCore12_glUniform1fE glUniform1f glUseProgram glShaderSource glLinkProgram _ZN6GLCore11_glIsShaderE glIsShader _ZN6GLCore12_glIsProgramE glIsProgram _ZN6GLCore26_glGetVertexAttribPointervE glGetVertexAttribPointerv _ZN6GLCore20_glGetVertexAttribivE _ZN6GLCore20_glGetVertexAttribfvE glGetVertexAttribfv _ZN6GLCore20_glGetVertexAttribdvE glGetVertexAttribdv _ZN6GLCore15_glGetUniformivE glGetUniformiv _ZN6GLCore15_glGetUniformfvE glGetUniformfv glGetUniformLocation _ZN6GLCore18_glGetShaderSourceE glGetShaderSource glGetShaderInfoLog glGetShaderiv glGetProgramInfoLog glGetProgramiv _ZN6GLCore20_glGetAttribLocationE glGetAttribLocation _ZN6GLCore21_glGetAttachedShadersE glGetAttachedShaders _ZN6GLCore19_glGetActiveUniformE glGetActiveUniform _ZN6GLCore18_glGetActiveAttribE glGetActiveAttrib _ZN6GLCore15_glDetachShaderE glDetachShader glDeleteShader glDeleteProgram glCreateShader glCreateProgram glCompileShader glBindAttribLocation glAttachShader _ZN6GLCore22_glStencilMaskSeparateE glStencilMaskSeparate _ZN6GLCore22_glStencilFuncSeparateE glStencilFuncSeparate _ZN6GLCore20_glStencilOpSeparateE glStencilOpSeparate _ZN6GLCore14_glDrawBuffersE glDrawBuffers _ZN6GLCore24_glBlendEquationSeparateE _ZN6GLCore20_glGetBufferPointervE glGetBufferPointerv _ZN6GLCore23_glGetBufferParameterivE glGetBufferParameteriv _ZN6GLCore14_glUnmapBufferE glUnmapBuffer _ZN6GLCore12_glMapBufferE glMapBuffer _ZN6GLCore19_glGetBufferSubDataE glGetBufferSubData glBufferSubData glBufferData _ZN6GLCore11_glIsBufferE glIsBuffer glGenBuffers glDeleteBuffers glBindBuffer _ZN6GLCore20_glGetQueryObjectuivE glGetQueryObjectuiv _ZN6GLCore19_glGetQueryObjectivE glGetQueryObjectiv _ZN6GLCore13_glGetQueryivE glGetQueryiv _ZN6GLCore11_glEndQueryE glEndQuery _ZN6GLCore13_glBeginQueryE glBeginQuery _ZN6GLCore10_glIsQueryE glIsQuery _ZN6GLCore16_glDeleteQueriesE glDeleteQueries _ZN6GLCore13_glGenQueriesE glGenQueries _ZN6GLCore19_glPointParameterivE glPointParameteriv _ZN6GLCore18_glPointParameteriE glPointParameteri _ZN6GLCore19_glPointParameterfvE glPointParameterfv _ZN6GLCore18_glPointParameterfE glPointParameterf _ZN6GLCore20_glMultiDrawElementsE glMultiDrawElements _ZN6GLCore18_glMultiDrawArraysE glMultiDrawArrays _ZN6GLCore20_glBlendFuncSeparateE _ZN6GLCore24_glGetCompressedTexImageE glGetCompressedTexImage _ZN6GLCore26_glCompressedTexSubImage1DE glCompressedTexSubImage1D _ZN6GLCore26_glCompressedTexSubImage2DE glCompressedTexSubImage2D _ZN6GLCore26_glCompressedTexSubImage3DE glCompressedTexSubImage3D _ZN6GLCore23_glCompressedTexImage1DE glCompressedTexImage1D _ZN6GLCore23_glCompressedTexImage2DE glCompressedTexImage2D _ZN6GLCore23_glCompressedTexImage3DE glCompressedTexImage3D _ZN6GLCore17_glSampleCoverageE glSampleCoverage glActiveTexture _ZN6GLCore20_glCopyTexSubImage3DE glCopyTexSubImage3D _ZN6GLCore16_glTexSubImage3DE glTexSubImage3D _ZN6GLCore13_glTexImage3DE _ZN6GLCore20_glDrawRangeElementsE glDrawRangeElements _ZN6GLCore16_glBlendEquationE _ZN6GLCore13_glBlendColorE glBlendColor _ZN6GLCore12_glIsTextureE _ZN6GLCore16_glTexSubImage2DE _ZN6GLCore16_glTexSubImage1DE _ZN6GLCore20_glCopyTexSubImage2DE _ZN6GLCore20_glCopyTexSubImage1DE _ZN6GLCore17_glCopyTexImage2DE _ZN6GLCore17_glCopyTexImage1DE _ZN6GLCore16_glPolygonOffsetE _ZN6GLCore14_glGetPointervE _ZN6GLCore15_glDrawElementsE _ZN6GLCore13_glDepthRangeE _ZN6GLCore25_glGetTexLevelParameterivE _ZN6GLCore25_glGetTexLevelParameterfvE _ZN6GLCore20_glGetTexParameterivE _ZN6GLCore20_glGetTexParameterfvE _ZN6GLCore14_glGetTexImageE _ZN6GLCore12_glGetStringE _ZN6GLCore11_glGetErrorE _ZN6GLCore13_glGetDoublevE _ZN6GLCore14_glGetBooleanvE _ZN6GLCore13_glReadPixelsE _ZN6GLCore13_glReadBufferE _ZN6GLCore14_glPixelStorefE _ZN6GLCore12_glDepthFuncE _ZN6GLCore12_glStencilOpE _ZN6GLCore14_glStencilFuncE _ZN6GLCore10_glLogicOpE _ZN6GLCore8_glFlushE _ZN6GLCore9_glFinishE _ZN6GLCore12_glDepthMaskE _ZN6GLCore12_glColorMaskE _ZN6GLCore14_glStencilMaskE _ZN6GLCore13_glClearDepthE _ZN6GLCore15_glClearStencilE _ZN6GLCore13_glClearColorE _ZN6GLCore8_glClearE _ZN6GLCore13_glDrawBufferE _ZN6GLCore13_glTexImage1DE _ZN6GLCore17_glTexParameterivE _ZN6GLCore16_glTexParameteriE _ZN6GLCore17_glTexParameterfvE _ZN6GLCore14_glPolygonModeE _ZN6GLCore12_glPointSizeE _ZN6GLCore7_glHintE g_NbOGLCoreFunc TwEventMouseButtonGLFW TwEventKeyGLFW g_KMod TwEventCharGLFW TwEventMouseButtonGLFWcdecl TwEventKeyGLFWcdecl TwEventCharGLFWcdecl TwEventMousePosGLFWcdecl TwEventMouseWheelGLFWcdecl TwEventMouseButtonGLUT TwEventMouseMotionGLUT TwGLUTModifiersFunc g_GLUTGetModifiers TwEventKeyboardGLUT TwEventSpecialGLUT TwEventSDL TwEventSDL13 TwEventSDL12 TwEventSFML TwEventX11 XLookupString buff_sz libgcc_s.so.1 libc.so.6 _edata __bss_start _end libAntTweakBar.so.1 GCC_3.0 GLIBC_2.14 GLIBC_2.2.5                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   4�         P&y   x�        B�         ���   ��     ui	   ��      �")            ��     �")            `�     �")            p�     �")            ��     �")            ��     �")            P�      #)             #)     �:)            �H     �:)            ��     �:)            8�     �:)            �	     �:)            �	     �:)            �	      ;)            �	     ;)            �	      ;)            �	     0;)            
     @;)            �     X;)            �     `;)            �     x;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �     �;)            �      <)            �     <)            �      <)            �     8<)            �     �<)            U)     �<)            R     �<)            i     �<)            {     �<)            �      =)            �     =)            �      =)            �     0=)            �     @=)            �     H=)                 P=)            �     `=)            �     h=)            �     p=)                 �=)                 �=)            �	     �=)            %     �=)            :     �=)            P     �=)            `     �=)            q     #)        -         �%)        -         #)        �           #)        �         �$)        �         �%)        �         �%)        �         (#)        K          0#)        F          H#)        F          �$)        F          P#)                  X#)                  `#)                  h#)                  p#)                  x#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �#)                  �&)                  �&)                  �&)                  �&)                  �#)        0          �#)        �          $)        [          $)        ?          $)        B           $)        �          ($)        v          0$)        �          8$)                  @$)        �          H$)        Z          P$)        f          X$)        �          `$)                  h$)        �          p$)        �          x$)        3          �$)        #          �$)        P          �$)                  �$)        �          �$)                  �$)        �          �$)        6          �$)        �          �$)        \          �$)        M           %)        �          %)        )          %)        �          %)        c           %)        |          (%)                  0%)        X          8%)        >          @%)        �          H%)        g          P%)        4          X%)        �          `%)        4          h%)        �          p%)        P          x%)        �          �%)        p          �%)        9          �%)        �          �%)        �          �%)        �          �%)        �          �&)        �          �%)        �          �%)        �          �%)        �          �%)        
           &)        &          &)        �          &)        :          &)        �           &)        f          (&)        �          0&)        �          8&)        �          @&)        z          H&)        �          P&)        k          X&)        A          `&)        .          h&)        �          �&)        X          �&)        X          �&)        	          �&)        �          �&)        �          �&)        l          �&)        2          �&)        �          �&)                   ')        G          ')        y          ')                  ')        ^           ')                  (')        \          0')        �          8')        �          @')        �          ())        �          0))        q          8))        �          @))        �          H))        �          P))        �          X))        �          `))        m          h))        �          p))                  x))        �          �))        d          �))        &           �))                  �))        �          �))                  �))        �          �))        �          �))        {          �))        �          �))        >           �))        C          �))        @           �))        �          �))        �          �))        �          �))        4           *)                  *)        f          *)        l          *)        U           *)        �          (*)                  0*)        �          8*)        g          @*)        �          H*)        h          P*)        H          X*)        �          `*)        �          h*)                   p*)                  x*)        W          �*)                  �*)        .          �*)                  �*)        >          �*)        �          �*)        �          �*)                   �*)        �          �*)        b          �*)        �          �*)        �          �*)        )          �*)        �           �*)        �          �*)        �          �*)        �           +)        �          +)        �          +)        G          +)        �           +)                  (+)        
          H,)        S          P,)        �          X,)        �          `,)        �          h,)        �          p,)        /          x,)        Z          �,)        �          �,)        �          �,)        ^          �,)        ^          �,)        �          �,)        V          �,)        -          �,)        �          �,)        �          �,)        �          �,)        /          �,)        �          �,)        �          �,)        �          �,)        �          �,)        �           -)        c          -)        �          -)        V          -)                   -)        �          (-)        n          0-)        �          8-)        �          @-)        �          H-)        �          P-)        �          X-)        �          `-)        _          h-)        �          p-)        �          x-)        i          �-)        �          �-)        �          �-)        �          �-)        �          �-)        '          �-)                  �-)        J          �-)        �          �-)        t          �-)        H          �-)        �          �-)        �          �-)        �          �-)        �          �-)        K          �-)        �           .)        h          .)        E          .)        �          .)        �           .)        �          (.)        `          0.)        I          8.)        �          @.)        �          H.)        �          P.)        H          X.)                  `.)        Z          h.)                  p.)                  x.)        �          �.)        n          �.)        �          �.)                  �.)                  �.)        �          �.)        
          �D)        N           �D)        �          �D)        �           E)        3          0M)        3          E)        �          8M)        �          E)        G          @M)        G          E)        �          HM)        �           E)        5           (E)        �           PM)        �           0E)        �          �L)        �          8E)        �          @E)        m          HE)        �          XN)        �          PE)        I          XE)        �          `E)                   hE)        k           pE)        @          �M)        @          xE)                  �M)                  �E)        �          �E)        C          �E)                   �E)        �           �E)        �          �E)                  �E)        E          �E)        �          �E)                  �E)        �           �E)        N          �E)        �           �E)        Q          �E)        L          �E)        �           �E)        �            F)        �           �M)        �           F)        2          F)        �          F)        �            F)                   (F)        Y          �L)        Y          0F)        �           �M)        �           8F)        9           �L)        9           @F)        �           HF)                  �M)                  PF)        *          �L)        *          XF)        y          �M)        y          `F)                  xM)                  hF)        �          �L)        �          pF)        �           xF)        @          `N)        @          �F)        K          �L)        K          �F)        �          �L)        �          �F)        d           �L)        d           �F)        (           �L)        (           �F)                  �F)        �           �F)        6           �F)        �          �M)        �          �F)        �          �F)        m           �F)        �          �F)        D           �F)        �           �F)        q          �F)                  �F)        �           G)                  G)                  G)        �           G)        V            G)        N          (G)        _           0G)        D          8G)        Q           @G)        *           HG)        G          PG)        �           XG)        	          `G)                  hG)                  pG)        a           xG)        I           �G)                  �G)                   �G)        �           �G)        9          �G)                   �G)        ;          �G)        U          �G)        7          �G)        Y           �G)        �           �M)        �           �G)        q           �G)        �          �M)        �          �G)        "          �M)        "          �G)        �           �G)        Z           �M)        Z           �G)        &           H)        f          H)        -           �M)        -           H)        a          H)        �          �L)        �           H)        =          (H)        �          0H)        k          8H)        �          @H)        !          HH)                  PH)                  XH)        ~           `H)        %          hH)        /          pH)        �           xH)        t          �H)        �           �H)        �          �H)        �           �H)        �          �H)                  �H)        �           �H)        `          �H)        0          �H)        �          �H)        P          �H)        �           �H)        ^           �H)        �          �H)        �           �H)                   �H)        7           I)        �           I)        9          I)        Q          I)                    I)        #           (I)        ^          0I)        �           8I)        c          @I)        �          HI)        [           PI)        d          XI)        �          `I)        �           hI)        �          pI)        �           xI)        w           �I)        �           �I)        �          �I)        J           �I)        �           �I)        "           �I)        �           �I)        ,          �I)                  �I)        0           �I)                   �I)        �           �I)                  �I)                   �I)        }          �I)        �          �I)        �            J)        ?          J)        U           J)        �           J)        �            J)        �          (J)        �          0J)        �          8J)        _          @J)        �           HJ)        !          PJ)        h           XJ)        �           `J)        �           hJ)                  pJ)        2           xJ)                  �J)        �           �J)                   �J)                  �J)                  �J)        �          �J)        �           �J)        ~          �J)        �          �J)        �           �J)        �          �J)                  �J)        T          �J)                  �J)                  �J)        O          �J)        �           K)        �          K)                   K)        L           K)                   K)        B          (K)        �           0K)        P          8K)        �          @K)        *          HK)        �           PK)        S          XK)        ]           `K)        \           hK)        �          pK)                   xK)        �          �K)        	          �K)        �          �K)        H          �K)        =          �K)        �           �K)        |          �K)        �           �K)        �          �K)                  �K)        }           �K)        �           �K)        R           �K)        
           @0)        �          H0)        $          P0)                   X0)        m          `0)                  h0)                  p0)        �          x0)        �          �0)        �          �0)        �          �0)                   �0)        �          �0)        �          �0)        c          �0)        +           �0)        t          �0)        1           �0)        I          �0)        4           �0)        �          �0)        8           �0)        �          �0)        ;           �0)        =            1)        �          1)        +          1)        �          1)        `           1)        C           (1)        `          01)        E           81)        G           @1)        H           H1)        �          P1)        �          X1)        �          `1)        e          h1)        M           p1)        O           x1)        P           �1)        S           �1)        �          �1)        �          �1)        �          �1)        �          �1)                  �1)        �          �1)        �          �1)        w          �1)                  �1)        `           �1)        �          �1)        	          �1)                  �1)        f           �1)        w           2)        l           2)        Q          2)        o           2)        �           2)                  (2)        t           02)        �          82)        �          @2)        v           H2)        y           P2)        �          X2)        �          `2)        �          h2)        �          p2)        �           x2)        F          �2)        �          �2)        �           �2)        �           �2)        %          �2)        �          �2)                  �2)        �           �2)        �           �2)        =          �2)        4          �2)        �          �2)        *          �2)        �           �2)        �          �2)        >          �2)        ]           3)        n          3)        �           3)                  3)        �           3)        �          (3)        �           03)        �           83)        �          @3)        �           H3)        �          P3)        R          X3)        �           `3)        �          h3)        �           p3)        �          x3)        �           �3)        O          �3)        �          �3)        �           �3)        /          �3)        �           �3)        �          �3)        �           �3)        �          �3)        �          �3)                  �3)        �           �3)        �          �3)        �           �3)        �          �3)        @          �3)        �           4)        �          4)        �           4)        �           4)        �            4)        �           (4)        X          04)        �           84)        �           @4)        D          H4)        �           P4)        3          X4)        d          `4)        �          h4)        �          p4)        �           x4)        J          �4)        �           �4)        D          �4)        �           �4)        �           �4)        v          �4)        �           �4)        �          �4)        y          �4)        �          �4)        �          �4)        Q          �4)        �          �4)        �          �4)        �          �4)        D          �4)        N           5)                  5)                  5)        /          5)        �           5)        ?          (5)        >          05)                  85)        �          @5)        #          H5)        �          P5)        $          X5)        %          `5)        �          h5)        t          p5)        )          x5)        �          �5)        -          �5)        �          �5)        �          �5)        /          �5)        �          �5)        5          �5)        8          �5)        <          �5)        k          �5)        �          �5)        8          �5)        8          �5)        R          �5)        �          �5)        X          �5)        [           6)        �          6)        O          6)        ~          6)        6           6)        W          (6)        8          06)        ,          86)        �          @6)        i          H6)        �          P6)        �          X6)        e          `6)        �          h6)        d          p6)        s          x6)        �          �6)        g          �6)        v          �6)        7          �6)        w          �6)        B          �6)        P          �6)        �          �6)        r          �6)        �          �6)        J          �6)        �          �6)        2          �6)        �          �6)        �          �6)        �          �6)        �           7)        q          7)        �          7)        �          7)        �           7)        �          (7)        �          07)        �          87)        �          @7)        �          H7)        �          P7)        �          X7)        c          `7)        �          h7)        �          p7)        �          x7)        �          �7)        �          �7)        �          �7)        �          �7)        �          �7)        G          �7)        1          �7)        S          �7)        �          �7)        	          �7)        �          �7)        �          �7)        �          �7)                  �7)        �          �7)        �          �7)        S           8)        �          8)        �          8)        t          8)        �           8)        �          (8)        �          08)        �          88)        u          @8)        �          H8)        _          P8)        �          X8)        �          `8)        �          h8)        $          p8)        m          x8)        �          �8)        �          �8)        �          �8)        �          �8)        �          �8)        �          �8)        N          �8)        �          �8)        C          �8)        �          �8)        Y          �8)        .          �8)        �          �8)                   �8)                  �8)        ,          �8)                   9)        `          9)                  9)                  9)        j           9)                  (9)        �          09)        
          89)        T          @9)        �          H9)        	          P9)        "          X9)        E          `9)        &          h9)        �          p9)        "          x9)        "          �9)        �          �9)        �          �9)        @          �9)        �          �9)        )          �9)        �          �9)        Q          �9)        ,          �9)        �          �9)        :          �9)        �          �9)        1          �9)                  �9)        �          �9)        4          �9)        5           :)        6          :)        8          :)        }          :)        >           :)        C          (:)        S          0:)        V          8:)        J          @:)        *          H:)        �          P:)        u          X:)        O          `:)        ;          h:)        F          p:)        U          x:)        X          �:)        Z          H��H��K' H��t��
  H���              �5�N' �%�N' @ �%�N' h    ������%�N' h   ������%�N' h   ������%�N' h   �����%�N' h   �����%�N' h   �����%�N' h   �����%�N' h   �p����%�N' h   �`����%�N' h	   �P����%�N' h
   �@����%�N' h   �0����%�N' h   � ����%zN' h
N' h   �0����%N' h   � ����%�M' h   �����%�M' h   � ����%�M' h   ������%�M' h    ������%�M' h!   ������%�M' h"   ������%�M' h#   �����%�M' h$   �����%�M' h%   �����%�M' h&   �����%�M' h'   �p����%�M' h(   �`����%�M' h)   �P����%�M' h*   �@����%�M' h+   �0����%�M' h,   � ����%zM' h-   �����%rM' h.   � ����%jM' h/   ������%bM' h0   ������%ZM' h1   ������%RM' h2   ������%JM' h3   �����%BM' h4   �����%:M' h5   �����%2M' h6   �����%*M' h7   �p����%"M' h8   �`����%M' h9   �P����%M' h:   �@����%
M' h;   �0����%M' h<   � ����%�L' h=   �����%�L' h>   � ����%�L' h?   ������%�L' h@   ������%�L' hA   ������%�L' hB   ������%�L' hC   �����%�L' hD   �����%�L' hE   �����%�L' hF   �����%�L' hG   �p����%�L' hH   �`����%�L' hI   �P����%�L' hJ   �@����%�L' hK   �0����%�L' hL   � ����%zL' hM   �����%rL' hN   � ����%jL' hO   ������%bL' hP   ������%ZL' hQ   ������%RL' hR   ������%JL' hS   �����%BL' hT   �����%:L' hU   �����%2L' hV   �����%*L' hW   �p����%"L' hX   �`����%L' hY   �P����%L' hZ   �@����%
L' h[   �0����%L' h\   � ����%�K' h]   �����%�K' h^   � ����%�K' h_   ������%�K' h`   ������%�K' ha   ������%�K' hb   ������%�K' hc   �����%�K' hd   �����%�K' he   �����%�K' hf   �����%�K' hg   �p����%�K' hh   �`����%�K' hi   �P����%�K' hj   �@����%�K' hk   �0����%�K' hl   � ����%zK' hm   �����%rK' hn   � ����%jK' ho   ������%bK' hp   ������%ZK' hq   ������%RK' hr   ������%JK' hs   �����%BK' ht   �����%:K' hu   �����%2K' hv   �����%*K' hw   �p����%"K' hx   �`����%K' hy   �P����%K' hz   �@����%
K' h{   �0����%K' h|   � ����%�J' h}   �����%�J' h~   � ����%�J' h   ������%�J' h�   ������%�J' h�   ������%�J' h�   ������%�J' h�   �����%�J' h�   �����%�J' h�   �����%�J' h�   �����%�J' h�   �p����%�J' h�   �`����%�J' h�   �P����%�J' h�   �@����%�J' h�   �0����%�J' h�   � ����%zJ' h�   �����%rJ' h�   � ����%jJ' h�   ������%bJ' h�   ������%ZJ' h�   ������%RJ' h�   ������%JJ' h�   �����%BJ' h�   �����%:J' h�   �����%2J' h�   �����%*J' h�   �p����%"J' h�   �`����%J' h�   �P����%J' h�   �@����%
J' h�   �0����%J' h�   � ����%�I' h�   �����%�I' h�   � ����%�I' h�   ������%�I' h�   ������%�I' h�   ������%�I' h�   ������%�I' h�   �����%�I' h�   �����%�I' h�   �����%�I' h�   �����%�I' h�   �p����%�I' h�   �`����%�I' h�   �P����%�I' h�   �@����%�I' h�   �0����%�I' h�   � ����%zI' h�   �����%rI' h�   � ����%jI' h�   ������%bI' h�   ������%ZI' h�   ������%RI' h�   ������%JI' h�   �����%BI' h�   �����%:I' h�   �����%2I' h�   �����%*I' h�   �p����%"I' h�   �`����%I' h�   �P����%I' h�   �@����%
I' h�   �0����%I' h�   � ����%�H' h�   �����%�H' h�   � ����%�H' h�   ������%�H' h�   ������%�H' h�   ������%�H' h�   ������%�H' h�   �����%�H' h�   �����%�H' h�   �����%�H' h�   �����%�H' h�   �p����%�H' h�   �`����%�H' h�   �P����%�H' h�   �@����%�H' h�   �0����%�H' h�   � ����%zH' h�   �����%rH' h�   � ����%jH' h�   ������%bH' h�   ������%ZH' h�   ������%RH' h�   ������%JH' h�   �����%BH' h�   �����%:H' h�   �����%2H' h�   �����%*H' h�   �p����%"H' h�   �`����%H' h�   �P����%H' h�   �@����%
H' h�   �0����%H' h�   � ����%�G' h�   �����%�G' h�   � ����%�G' h�   ������%�G' h�   ������%�G' h�   ������%�G' h�   ������%�G' h�   �����%�G' h�   �����%�G' h�   �����%�G' h�   �����%�G' h�   �p����%�G' h�   �`����%�G' h�   �P����%�G' h�   �@����%�G' h�   �0����%�G' h�   � ����%zG' h�   �����%rG' h�   � ����%jG' h�   ������%bG' h�   ������%ZG' h�   ������%RG' h�   ������%JG' h�   �����%BG' h�   �����%:G' h�   �����%2G' h�   �����%*G' h�   �p����%"G' h�   �`����%G' h�   �P����%G' h�   �@����%
G' h�   �0����%G' h�   � ����%�F' h�   �����%�F' h�   � ����%�F' h�   ������%�F' h   ������%�F' h  ������%�F' h  ������%�F' h  �����%�F' h  �����%�F' h  �����%�F' h  �����%�F' h  �p����%�F' h  �`����%�F' h	  �P����%�F' h
  �@����%�F' h  �0����%�F' h  � ����%zF' h
F' h  �0����%F' h  � ����%�E' h  �����%�E' h  � ����%�E' h  ������%�E' h   ������%�E' h!  ������%�E' h"  ������%�E' h#  �����%�E' h$  �����%�E' h%  �����%�E' h&  �����%�E' h'  �p����%�E' h(  �`����%�E' h)  �P����%�E' h*  �@����%�E' h+  �0����%�E' h,  � ����%zE' h-  �����%rE' h.  � ����%jE' h/  ������%bE' h0  ������%ZE' h1  ������%RE' h2  ������%JE' h3  �����%BE' h4  �����%:E' h5  �����%2E' h6  �����%*E' h7  �p����%"E' h8  �`����%E' h9  �P����%E' h:  �@����%
E' h;  �0����%E' h<  � ����%�D' h=  �����%�D' h>  � ����%�D' h?  ������%�D' h@  ������%�D' hA  ������%�D' hB  ������%�D' hC  �����%�D' hD  �����%�D' hE  �����%�D' hF  �����%�D' hG  �p����%�D' hH  �`����%�D' hI  �P����%�D' hJ  �@����%�D' hK  �0����%�D' hL  � ����%zD' hM  ����PH�Hx�w ���=���ZÐPH�Hx�w ���'���ZÐH�
��@ (�(��X��Y��\��c����(�(��"���D  (�������     �\��Y��^��X��
�f.�     �\��Y��^��X��'�����    �\��Y��^��X��������    �X@H ���� �X0H �%��� �X% H ���� (��&����     (������     (��0���(�(��X��F���f�     ��A��A���*�AT�A*�M���A*�UH��SL��H��0�
�    H��@��t
1�H��f�G���t� H��  �   @����  @����  @���
  ��1���@���H�t
�    H��@��t
1�H��f�G���t� H��  �   @����  @����  @���  ��1���@���H�t
�    H��@��tE1�H��fD�W���t� H��  �   @����  @����  @���   ��1���@���H�t
�    H��@��tE1�H��fD�G���t� H��  �   @����  @����  @����  ��1���@���H�t
�    H��@��t
1�H��f�G���t� �B    �B    H�    ǂ      ǂ      �fD  H�z�B f��@���
���f.�     1�H����f�G�@�������f�     �    ��H�������fD  H��  Ƃ   f��@������@ 1�H����f�G�@���������    ��H�������fD  H��  Ƃ   f��@������@ E1�H����fD�_�@�������    �    ��H�������fD  H��  Ƃ   f��@������@ E1�H����fD�O�@�������    �    ��H�������fD  H��  Ƃ   f��@��� ���@ 1�H����f�O�@��������    ��H�������@ f.�     SH��H�?H��t����H�    �C    �C    ǃ      [�@ f.�     AWAVAUATUSH��H��  ��H��$p  ��$x  ��$|  ��  Hc�E1�H��1�E1�1��Df.�     E���  A9�t
  ����  ���[���  ������  ��  ��  ��  ���[��  ��  ��  ��  ��  �[��  �   ��  �   ��  ��  �    E1�)���H��   ���<�    L�,L��   L��   L��   L��   1���  A���� A)D ��
  �� AAD��  �� AAD��  �� AADfn�  fp� fo�fr�f��fr��AH��A9��s����)�9��  Hc���  H���@��
  ��  ��  ��  ��  ��  ��  �����������  �F��   H���  H�����@��
  ��  ��  ��  ��  ��  ��  �����������  tYHc���  H���@��
  ��  ��  ��  ��  ��  ��  ���������  H������H��H������H������@ �D$�D$ �]���H�
A��$l  ��H��' H�H��tA�t$0���  ��A�t$4���  �H�m' H�8 tA�|$8 �&  A�|$9 �  H��' H�8 tH�{' H� H��tA�|$<��H�U' H�8 tA�|$@ ��  A��$L   tA��$M   ��  H�:' H� H��t
A��$P  ��H��' H� H��tA��$X  A��$T  ��H��' H� H��t"A��$h  A��$d  A��$`  A��$\  ��H�' A�t$ �  �A�t$$�  �H��' A�T$� "  � #  �H��
' �H�E H���   []A\A]A^A_��f�     H��
' �   �H�-�' ����H�
' �  �H�' �D  �A���J  A���  H�9' �D  H�D$�H�^' H�D$H�D$��
' H�D$�����H�
' ������H�&
' ������L�-x	' H�	' I�}  �  L�<$H��H�D$���  I��D  �I��H  ��  �H�D$0�D$0   �q�  H�D$H����D$0���T  �����  H�$H�R' E1�H�-�	' L�`A@ A����  I��A��A�U ��
�& ƅL   H�H���   []�@ H���& �D$H�2H���& H�:�;����D$H��[]�H��� uQ�G���$u4臵�����   uH�O�& H�0H���& H�8�����1҉�H���@ H�� ' H��   ��H�
�BH��H)�H��I��I��H��~D  H�T��H�T��H��L9�u���FH��[]A\A]Ð1��H�H)�H��H����   H��A�   H)�H��L��H�T$H�4$訫��H�4$H�T$H�HI��I��H��tH�H�H�H9���   H��L���    H��tL�L�H��H��H9�u�H��H��H)�H��H��I�l�L�KL9�t1H��H�� H��tL�L�H��H��I9�u�H��I)�I��J�l�H�;H��t�ۛ��L�#M�H�kL�c�����H�< H9�vH��I������H)�H������H���I��������L9�w�H��I��H)�I��H��H��������   E1������f.�     @ AVAUATUH��SH��H��H�GH;GtTH��tG�H��H�H�GH�C�H�W�H)�H��H��tH��H��H)�������] H��[]A\A]A^�D  1��@ H�H)�H��H����   H�4 H9���   I��I������I)�I��L��H�T$�ҩ��H�T$I��K��H��t��H�3H��E1�H)�H��H��tL�$�    L��L���e���H�KO�D&E1�H)�H��H��tL�$�    L��H��L���7���I��H�;M�H��t�4���M�L�3L�cL�k�����I��A�   I)�I���L���H��������?H9��(���I��E1�L�,�    I)�I��H���0�������D  H���D  AUATUSH��H��(L�GH�GL)�H��H9��  L��I��H)��	H���AH9��  H��M��I)�M9��0  L��L��f.�     H��tH�9H�8H��H��I9�u�H�CL��H�H)�H�C1�H��H��H��H��~fD  I�L��I�L��H��H9�u�H�H9�txH�FH��H)�H��H��H��H��H��I��M�t5H��v/(�1���I��H��I��H9�BBTw�I9�N��t I�A�A	�AAH9�t�AI�AAH��([]A\A]�ÐH)��/  H��L���H��t	��@H��H��u�H�CH��I9�H�C�  H��H��tH�
H�H��H��I9�u�H�FH��H{L��H)�H��H��H��H��H��H��H�t9H��v3(�1���I��H��I��H9�BBTr�H9�L���7���I�A�A	�AAI9����������    H�H��������I��I)�I��M)�L9��Z  I9�H��I��IC�I)�I��I��   H������H��H�L$H�T$H�t$�b���H�t$H�T$I��H�L$O��I��f�     M��tH�I� I��I��u�H�H9���   I��L��f�     H��tM�L�	I��H��L9�u�H��H��H)�H��H��I�D�L�,�H�CH9�t5H��L���    H��tL�L�H��H��H9�u�H��H)�H��M�l�H�;H��t�s���L�L�#L�kH�k�����I9������M��u;1�E1�����L�������L�������H��H�C����L���a���H�=� 藧��J�,�    ����f.�     �GÐf.�     SH��H���G���$tH���& H��   ��C    H�C    H��[��     �D$A�D���   Љ��   H�H���   ��@ f.�     H�Ǉ�       E1�Ǉ�       1�1�1�H���   ���fD  AVAUATUS� H����  ��A����  ��A����  H�-n�& �G���   ���   H�wHǇ�       Ǉ�       ��  �U H�?�& A�M�A�T$�1�1��H�s$�C$    ���  �U L�5>�& 1�A�H���& H�s�!  �C  �?�H�	�& �!� �L�%8�& �   A�$L�-H�& �C(�   A�U �D  A�$�D  �C)A�U �q  A�$�q  �C*A�U ��  A�$�C+H�W�& ��  ��  A�$�  �C4A�U H�s8�  �U H�s,��  �U H�s0��  �U H�(�& �  �  �H�s �C     �i�  �U H���& 1���
���H�
f��I�O8I�w@�T$H�����H�T$ H��$�   H����e�������H�T$ H��$�   �D$H��e��I�O8I�w@�D$H����H�T$ H��$�   �D$�L$H�e��I�O8I�w@�D$�L$H����H�T$ H��$�   �D$L�L$�T$H�We��I�O8I�w@�D$L�L$�T$H����H�T$ H��$�   �L$�T$H�e��I�O8I�w@�L$�T$H�#���1��i���H�T$ H��$�   H���Un��I�OhI�wpH9��T���H�T$ H��$�   �2n��I�OhI�wpH9��X���H�T$ H��$�   �n��I�OhI�wpH9��\���H�T$ H��$�   ��m��I�OhI�wpH9��`���H�T$ H��$�   ��m��I�OhI�wpH9��d���H�T$ H��$�   H���m���d���H���& D�L$HH��$�   �   ��D$H�q���H�
�BH��H)�H��I��I��H��~D  H�T��H�T��H��L9�u���FH��[]A\A]Ð1��H�H)�H��H����   H��A�   H)�H��L��H�T$H�4$�p��H�4$H�T$H�HI��I��H��tH�H�H�H9���   H��L���    H��tL�L�H��H��H9�u�H��H��H)�H��H��I�l�L�KL9�t1H��H�� H��tL�L�H��H��I9�u�H��I)�I��J�l�H�;H��t�;`��L�#M�H�kL�c�����H�< H9�vH��I������H)�H������H���I��������L9�w�H��I��H)�I��H��H��������   E1������f.�     @ H���D  AUATUSH��H��(L�GH�GL)�H��H9��  L��I��H)��	H���AH9��  H��M��I)�M9��0  L��L��f.�     H��tH�9H�8H��H��I9�u�H�CL��H�H)�H�C1�H��H��H��H��~fD  I�L��I�L��H��H9�u�H�H9�txH�FH��H)�H��H��H��H��H��I��M�t5H��v/(�1���I��H��I��H9�BBTw�I9�N��t I�A�A	�AAH9�t�AI�AAH��([]A\A]�ÐH)��/  H��L���H��t	��@H��H��u�H�CH��I9�H�C�  H��H��tH�
H�H��H��I9�u�H�FH��H{L��H)�H��H��H��H��H��H��H�t9H��v3(�1���I��H��I��H9�BBTr�H9�L���7���I�A�A	�AAI9����������    H�H��������I��I)�I��M)�L9��Z  I9�H��I��IC�I)�I��I��   H������H��H�L$H�T$H�t$�Rl��H�t$H�T$I��H�L$O��I��f�     M��tH�I� I��I��u�H�H9���   I��L��f�     H��tM�L�	I��H��L9�u�H��H��H)�H��H��I�D�L�,�H�CH9�t5H��L���    H��tL�L�H��H��H9�u�H��H)�H��M�l�H�;H��t�c\��L�L�#L�kH�k�����I9������M��u;1�E1�����L�������L�������H��H�C����L���a���H�=�h �m��J�,�    ����f.�     �8
H��t��� �l fW�f(���fD  �Gx�*��Gy�*��Gz�*��fD  �Wx�O|���   Z�Z�Z�� �Wx���   ���   �n���fD  �Gx�*��Gz�*��G|�*��K��� �Gx�*��Gz�*��G|�*��+��� �*Wx�*O|�*��   ������Gx�H*ЋG|�H*ȋ��   �H*�������k �8� �