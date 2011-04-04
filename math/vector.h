#ifndef MATH_VECTOR_H
#define MATH_VECTOR_H

#include <iostream>
#include <valarray>


namespace math {
  /*
    Represents a simple vector in n-dimensional euclidian space.
  */
  class Vector {
  public:
    typedef double Type;

    static const Type kZero = 0;
    static const Type kEpsilon = 1e-9;

    Vector(int size);
    Vector(int size, const Type *components);
    Vector(const Vector &other);

    int Size(void) const;
    Type Distance(const Vector &other) const;

    Vector& operator = (const Vector &other) const;
    Type& operator [] (int index);
    const Type& operator [] (int index) const;

    friend std::istream& operator >> (std::istream &is, Vector &v);
    friend std::ostream& operator << (std::ostream &os, Vector &v);

  private:
    std::valarray<Type> components_;
    const int size_;

    static bool EQ(Type u, Type v);
  }; // class Vector

} // namespace math

#endif
