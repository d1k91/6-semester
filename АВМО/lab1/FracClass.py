def gcd(m, n):
    while m % n != 0:
        oldm = m
        oldn = n
        m = oldn
        n = oldm % oldn
    return n

class frac:
    def __init__(self, top, bot):
        if bot == 0:
            raise ValueError("Знаменатель не может быть равен 0.")
        
        if top == 0:
            self.is_zero = True
            self.value = 0
            self.num = top
            self.den = bot
        elif bot == 1:
            self.is_zero = False
            self.is_integer = True
            self.value = top
            self.num = top
            self.den = bot
        elif top == bot:
            self.is_zero = False
            self.is_integer = True
            self.value = top // bot
            self.num = top
            self.den = bot
        else:
            self.is_zero = False
            self.is_integer = False
            if isinstance(top, frac) and isinstance(bot, frac):
                self.num = top.num*bot.den
                self.den = top.den*bot.num
                com = gcd(self.num, self.den)
                self.den //= com
                self.num //= com
                if self.num < 0 and self.den < 0:
                    self.num *= -1
                    self.den *= -1
            else:
                self.num = top
                self.den = bot
                if self.num < 0 and self.den < 0:
                    self.num *= -1
                    self.den *= -1

    def __str__(self):
        if self.is_zero:
            return "0"
        elif self.is_integer:
            return str(self.value)
        elif self.den == 1:
            return str(self.num)
        else:
            return f"{self.num}/{self.den}"

    def __add__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        newnum = self.num * other.den + other.num * self.den
        newden = self.den * other.den
        com = gcd(newnum, newden)
        return frac(newnum // com, newden // com)

    def __radd__(self, other):
        return self.__add__(other)

    def __sub__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        newnum = self.num * other.den - other.num * self.den
        newden = self.den * other.den
        com = gcd(newnum, newden)
        return frac(newnum // com, newden // com)

    def __rsub__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        return other.__sub__(self)

    def __eq__(self, value):
        if isinstance(value, int):
            value = frac(value, 1)
        if self.is_zero and value.is_zero:
            return True
        elif self.is_zero or value.is_zero:
            return False
        else:
            first = self.num * value.den
            second = self.den * value.num
            return first == second
        
    def __lt__(self,other):
        if isinstance(other,int):
            other = frac(other,1)
        num = self.num * other.den
        onum = self.den * other.num
        if num < onum:
            return True
        else:
            return False
        
    def __gt__(self,other):
        if isinstance(other,int):
            other = frac(other,1)
        num = self.num * other.den
        onum = self.den * other.num
        if num > onum:
            return True
        else:
            return False

    def __mul__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        newnum = self.num * other.num
        newden = self.den * other.den
        com = gcd(newnum, newden)
        return frac(newnum // com, newden // com)

    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        if other.is_zero:
            raise ZeroDivisionError("Нельзя делить на 0.")
        newnum = self.num * other.den
        newden = self.den * other.num
        com = gcd(newnum, newden)
        return frac(newnum // com, newden // com)

    def __rtruediv__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        return other.__truediv__(self)
    
    def __floordiv__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        if other.is_zero:
            raise ZeroDivisionError("Нельзя делить на 0.")
        newnum = self.num * other.den
        newden = self.den * other.num
        com = gcd(newnum, newden)
        return frac(newnum // com, newden // com)

    def __rfloordiv__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        return other.__rfloordiv__(self)
    
    def __mod__(self, other):
        if isinstance(other, int):
            other = frac(other, 1)
        num = self.num * other.den
        onum = self.den * other.num
        den = self.den * other.den
        return frac(num%onum, den)
    
    def __rmod__(self,other):
        if isinstance(other, int):
            other = frac(other, 1)
        return other.__mod__(self)

    
    
        

